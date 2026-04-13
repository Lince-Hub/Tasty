package lt.linas_puplauskas.tasty.fxControllers.component.order;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lt.linas_puplauskas.model.restaurant.Dish;

import java.util.List;

public class ItemHandler {

    private final TableView<Dish> itemsTable;
    private final TableColumn<Dish, String> titleCol;
    private final TableColumn<Dish, String> categoryCol;
    private final TableColumn<Dish, Integer> priceCol;
    private final TableColumn<Dish, Integer> amountCol;
    private final TableColumn<Dish, Integer> caloriesCol;
    private final TableColumn<Dish, Float> weightCol;

    public ItemHandler(TableView<Dish> itemsTable,
                       TableColumn<Dish, String> titleCol,
                       TableColumn<Dish, String> categoryCol,
                       TableColumn<Dish, Integer> priceCol,
                       TableColumn<Dish, Integer> amountCol,
                       TableColumn<Dish, Integer> caloriesCol,
                       TableColumn<Dish, Float> weightCol) {
        this.itemsTable = itemsTable;
        this.titleCol = titleCol;
        this.categoryCol = categoryCol;
        this.priceCol = priceCol;
        this.amountCol = amountCol;
        this.caloriesCol = caloriesCol;
        this.weightCol = weightCol;

        setupTableColumns();
    }

    private void setupTableColumns() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        caloriesCol.setCellValueFactory(new PropertyValueFactory<>("calories"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    }

    public void fill(List<Dish> items) {
        clear();
        if (items != null && !items.isEmpty()) {
            itemsTable.getItems().setAll(items);
        }
    }

    public void clear() {
        itemsTable.getItems().clear();
    }

    public List<Dish> getItems() {
        return itemsTable.getItems();
    }

    public void addItem(Dish item) {
        if (item != null) {
            itemsTable.getItems().add(item);
        }
    }

    public void removeItem(Dish item) {
        itemsTable.getItems().remove(item);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < itemsTable.getItems().size()) {
            itemsTable.getItems().remove(index);
        }
    }
}