package com.dermabalance.presenters;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dermabalance.DermaApplication;
import com.dermabalance.R;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Reader;
import com.dermabalance.models.ReaderModel;
import com.dermabalance.utils.FileUtils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
    public void getProducts(String description) {
        model.getProductsLike("%" + description + "%");
    }

    @Override
    public void productsLikeGot(List<Product> products) {
        view.productsLikeGot(products);
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
    public void readExcelData(final int type, final Uri fileUri) {
        new ReadFileTask(type, fileUri).execute();
    }

    @Override
    public void getProduct(final String barcode) {
        model.getProductByBarcode(barcode);
    }

    @Override
    public void productGot(final Product product) {
        view.productGot(product);
    }

    @Override
    public void deleteData() {
        model.deleteData();
    }

    @Override
    public void dataDeleted() {
        view.dataDeleted();
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

        private Uri fileUri;

        public ReadFileTask(final int type, final Uri fileUri) {
            this.type = type;
            this.fileUri = fileUri;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            products = new ArrayList<>();

            String filePath = "";

            if (fileUri == null) {
                filePath = Environment.getExternalStorageDirectory()
                        + File.separator + DermaApplication.getInstance().getString(R.string.app_name) + "/precios.xls";
            } else {
                filePath = FileUtils.getPath(DermaApplication.getInstance().getBaseContext(), fileUri);
            }

            File inputFile = new File(filePath);

            try {
                final InputStream inputStream = new FileInputStream(inputFile);

                int rowsCount = 0;
                FormulaEvaluator formulaEvaluator;
                XSSFSheet sheet = null;
                HSSFSheet sheetOld = null;

                try {
                    final XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    sheet = workbook.getSheetAt(0);
                    rowsCount = sheet.getPhysicalNumberOfRows();
                    formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

                    return readDoc(rowsCount, sheet, null, formulaEvaluator);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final HSSFWorkbook workbookOld = new HSSFWorkbook(new FileInputStream(inputFile));
                sheetOld = workbookOld.getSheetAt(0);
                rowsCount = sheetOld.getPhysicalNumberOfRows();
                formulaEvaluator = workbookOld.getCreationHelper().createFormulaEvaluator();

                return readDoc(rowsCount, null, sheetOld, formulaEvaluator);

            } catch (final Exception e) {
                e.printStackTrace();
            }

            return false;
        }
        private boolean readDoc(int rowsCount, XSSFSheet sheet, HSSFSheet sheetOld, FormulaEvaluator formulaEvaluator) {
            for (int r = 2; r < rowsCount; r++) {
                Row row = null;
                if (sheet != null) {
                    row = sheet.getRow(r);
                } else {
                    row = sheetOld.getRow(r);
                }
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

            return true;
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
                final Product productDb = model.getProduct(product.getBarcode());
                final double newPrice = product.getPrice();
                if (productDb != null) {
                    final double initialPrice = productDb.getPrice();
                    double difference;
                    if (newPrice > initialPrice) {
                        difference = newPrice - initialPrice;
                    } else {
                        difference = initialPrice - newPrice;
                    }

                    if (difference > 1 && initialPrice > 0 && newPrice > 0) {
                        double newPriceRounded = getNewPrice(newPrice);
                        product.setPrice(newPriceRounded);
                        product.setLastPrice(initialPrice);
                        product.setDifference(newPriceRounded - initialPrice);
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

    private double getNewPrice(double newPrice) {
        int newPriceRounded = (int) Math.round(newPrice);
        return (double) newPriceRounded;
    }
}
