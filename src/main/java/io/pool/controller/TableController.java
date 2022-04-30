package io.pool.controller;

import io.pool.view.TableView;

public class TableController {

    /** Instance of the table that will be shown to the user */
    private TableView tableView;
    private GameController gameController;
    /** tableX and tableY */
    public static double tableX;
    public static double tableY;


    /**
     * Main constructor of TableController
     * @param tableView The Table View
     */
    public TableController(TableView tableView, GameController gameController) {
        this.tableView = tableView;
        this.gameController = gameController;
        tableX = tableView.getFullTable().getLayoutX()+(TableView.width/43.2);
        tableY = tableView.getFullTable().getLayoutY()+(TableView.width/16.);
    }


    /**
     * Gets the Table View
     * @return the Table View
     */
    public TableView getTableView() {
        return tableView;
    }


    public void turnView(GameController gameController){
        if(gameController.getP1().isTurn()){
            tableView.getPlayer1Lbl().setStyle("-fx-background-color: green");
            tableView.getPlayer1Lbl().setText(gameController.getP1().getUsername());
            tableView.getPlayer2Lbl().setStyle("-fx-background-color: #3D4956");
            tableView.getPlayer2Lbl().setText(gameController.getP2().getUsername());
        }else{
            tableView.getPlayer2Lbl().setStyle("-fx-background-color: green");
            tableView.getPlayer1Lbl().setText(gameController.getP1().getUsername());
            tableView.getPlayer2Lbl().setText(gameController.getP2().getUsername());
            tableView.getPlayer1Lbl().setStyle("-fx-background-color: #3D4956");
        }
    }

    public double getTableX() {
        return tableX;
    }

    public double getTableY() {
        return tableY;
    }


}
