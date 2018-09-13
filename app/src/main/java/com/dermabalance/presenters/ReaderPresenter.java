package com.dermabalance.presenters;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dermabalance.DermaApplication;
import com.dermabalance.R;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Reader;
import com.dermabalance.models.ReaderModel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReaderPresenter implements Reader.Presenter {

    private final static String TAG = "ReaderPresenter";

    private Reader.View view;

    private Reader.Model model;

    public static final int INSERT = 0, UPDATE = 1;

    public ReaderPresenter(Reader.View view) {
        this.view = view;
        model = new ReaderModel(this);
    }

    @Override
    public void getProducts() {
        model.getProducts();
    }

    @Override
    public void productsGot(final List<Product> products) {
        view.showProducts(products);
    }

    @Override
    public void readExcelData(final int type) {
        new ReadFileTask(type).execute();
    }

    /**
     * Returns the cell as a string from the excel file
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }

    class ReadFileTask extends AsyncTask<Void, Void, Boolean> {

        private List<Product> products;

        private int type;

        public ReadFileTask(final int type) {
            this.type = type;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            products = new ArrayList<>();

            String filePath = Environment.getExternalStorageDirectory()
                    + File.separator + DermaApplication.getInstance().getString(R.string.app_name) + "/precios.xls";

            File inputFile = new File(filePath);

            try {
                final InputStream inputStream = new FileInputStream(inputFile);
                final XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                final XSSFSheet sheet = workbook.getSheetAt(0);
                final int rowsCount = sheet.getPhysicalNumberOfRows();
                final FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

                for (int r = 2; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    //inner loop, loops through columns
                    String linea = "";
                    String barcode = "";
                    String descripcion = "";
                    String price = "";
                    for (int c = 0; c < cellsCount; c++) {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        switch (c) {
                            case 0:
                                linea = value;
                                break;
                            case 1:
                                barcode = value;
                                break;
                            case 2:
                                descripcion = value;
                                break;
                            case 3:
                                price = value;
                                break;
                        }
                    }
                    final Product product = new Product(linea, barcode, descripcion, price, 0);
                    if (products != null) {
                        products.add(product);
                    }
                }

                inputFile.delete();
                return true;

            } catch (final Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (type == INSERT) {
                    model.insertProducts(products);
                } else {
                    new ComparePricesTask(products).execute();
                }
            } else {
                view.showNoFile(type == INSERT);
            }
        }
    }

    class ComparePricesTask extends AsyncTask<Void, Void, Void> {

        private List<Product> productsFromExcel;

        private List<Product> productsChanged;

        public ComparePricesTask(final List<Product> productsFromExcel) {
            this.productsFromExcel = productsFromExcel;
            productsChanged = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Product product : productsFromExcel) {
                final Product productDb = model.getByBarcode(product.getBarcode());
                final double newPrice = product.getPrice();
                if (productDb != null) {
                    final double initialPrice = productDb.getPrice();
                    double difference = 0;
                    if (newPrice > initialPrice) {
                        difference = newPrice - initialPrice;
                    } else {
                        difference = initialPrice - newPrice;
                    }

                    if (difference > 1) {
                        product.setPrice(newPrice);
                        product.setLastPrice(initialPrice);
                        product.setDifference(newPrice - initialPrice);
                        product.setChanged(1);
                        productsChanged.add(product);
                        model.update(product);
                    }
                } else {
                    //New product
                    product.setChanged(1);
                    model.insertProduct(product);
                    productsChanged.add(product);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            view.showChanges(productsChanged);
        }
    }

}