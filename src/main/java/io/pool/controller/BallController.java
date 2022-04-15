package io.pool.controller;

import io.pool.Database.BallConfigurationDB;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.BallModel;
import io.pool.model.PhysicsModule;
import io.pool.model.PlayerModel;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import io.pool.view.TableView;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BallController {

    private ArrayList<Circle> holeList;
    private GameController gameController;
    private PoolCueController poolCueController;
    /**
     * ArrayList that contains all the BallViews
     */
    public static ArrayList<BallView> bViewList = new ArrayList<>();
    public static ArrayList<BallView> stripeBViewList = new ArrayList<>();
    public static ArrayList<BallView> solidBViewList = new ArrayList<>();
    /**
     * ArrayList that contains all the BallModels
     */
    public static ArrayList<BallModel> bModelList = new ArrayList<>();
    /**
     * ArrayList that contains all the stripe/solid
     */
    public static ArrayList<BallModel> stripeBModelList = new ArrayList<>();
    public static ArrayList<BallModel> solidBModelList = new ArrayList<>();
    public static ArrayList<BallModel> stripFull = new ArrayList<>();
    public static ArrayList<BallModel> solidFull = new ArrayList<>();
    private ArrayList<BallModel> bModelInEachTurn = new ArrayList<>();
    public static Boolean allInStripe = false;
    public static Boolean allInSolid = false;
    public static BallModel whiteBallModel;
    public static BallView whiteBallView;
    public static BallModel eightBallModel;
    public static BallView eightBallView;
    private boolean draggable = false;
    public boolean isMoving = false;
    public boolean isCollide = false;
    private boolean foul = false;
    private boolean scored = false;
    private boolean waitingForInput = true;
    private boolean firstPlay = false;
    private boolean setBallType = false;
    private BallModel firstCollide = null;
    private int counter = 0;


    public static ArrayList<Double> ballXPositions = new ArrayList();
    public static ArrayList<Double> ballYPositions = new ArrayList();


    double mouseAnchorX, mouseAnchorY;

    public BallController() {
    }

    public BallController(GameController gameController, ArrayList<Circle> holeList) {
        poolCueController = new PoolCueController(gameController.getGameView().getCueView(),gameController);
        this.gameController = gameController;

        if(holeList==null){
            this.holeList = gameController.getTableController().getTableView().getHoles();
        } else{
            this.holeList=holeList;
        }
        setBallPositions();
    }


    private void setBallPositions() {

        /**
         * Reference: https://billiardguides.com/how-to-rack-pool-balls/
         */
        double referenceX = 2 * gameController.getTableController().getTableView().getTableImageView().getFitWidth() / 3 + gameController.getTableController().getTableX();
        double referenceY = gameController.getTableController().getTableView().getTableImageView().getFitHeight() / 2 + gameController.getTableController().getTableY();

        ballXPositions.add(referenceX);
        ballXPositions.add(referenceX + 1 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 2 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 3 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 3 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 4 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 4 * (2 * BallModel.RADIUS));

        //eight-ball
        ballXPositions.add(referenceX + 2 * (2 * BallModel.RADIUS));
        //ballXPositions.add((double) TableView.width-60);

        ballXPositions.add(referenceX + 1 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 2 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 3 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 3 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 4 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 4 * (2 * BallModel.RADIUS));
        ballXPositions.add(referenceX + 4 * (2 * BallModel.RADIUS));

        //white-ball
        ballXPositions.add(gameController.getTableController().getTableView().getWhiteLine().getStartX() - BallModel.RADIUS + gameController.getTableController().getTableX());

        //////////////////////////////

        ballYPositions.add(referenceY);
        ballYPositions.add(referenceY + 1 * (BallModel.RADIUS));
        ballYPositions.add(referenceY - 2 * (BallModel.RADIUS));
        ballYPositions.add(referenceY - 1 * (BallModel.RADIUS));
        ballYPositions.add(referenceY + 3 * (BallModel.RADIUS));
        ballYPositions.add(referenceY + 2 * (BallModel.RADIUS));
        ballYPositions.add(referenceY - 4 * (BallModel.RADIUS));

        //eight-ball
        ballYPositions.add(referenceY);
        //ballYPositions.add((double) TableView.height+34);

        ballYPositions.add(referenceY - 1 * (BallModel.RADIUS));
        ballYPositions.add(referenceY + 2 * (BallModel.RADIUS));
        ballYPositions.add(referenceY + 1 * (BallModel.RADIUS));
        ballYPositions.add(referenceY - 3 * (BallModel.RADIUS));
        ballYPositions.add(referenceY);
        ballYPositions.add(referenceY + 4 * (BallModel.RADIUS));
        ballYPositions.add(referenceY - 2 * (BallModel.RADIUS));

        //white-ball
        ballYPositions.add(referenceY);


    }

    /**
     * Prepares the game by creating the BallModels and the BallViews. Adds the BallViews to the pane
     *
     * @param root Pane where the balls will be added
     */
    public void prepareGame(Pane root, TableView tView) {
        BallModel bModel;
        BallView bView;
        for (int i = 1; i <= 16; i++) {
            bModel = new BallModel(i);
            if (i == 16) {
                whiteBallModel = bModel;
            } else {
                if (i == 8) {
                    eightBallModel = bModel;
                } else if (i < 8) {
                    bModel.setBallType(0);
                    solidBModelList.add(bModel);
                } else {
                    bModel.setBallType(1);
                    stripeBModelList.add(bModel);
                }
            }

            bModel.setPositionX(new BigDecimal(ballXPositions.get(i - 1)));
            bModel.setPositionY(new BigDecimal(ballYPositions.get(i - 1)));


            bView = new BallView(ResourcesLoader.ballImages.get(i - 1), BallModel.RADIUS);
            if (i == 8) {
                eightBallView = bView;
            } else if (i < 8) {
                solidBViewList.add(bView);
            } else if (i == 16) {
                whiteBallView = bView;
                /**
                 * Getting the position of the mouse to set the new position of the white ball when dragged
                 */

                bView.getBall().setOnMousePressed(mouseEvent -> {

                    mouseAnchorX = mouseEvent.getX();
                    mouseAnchorY = mouseEvent.getY();
                });

                /** Assigning new variables because it will show an error if we use the same variables
                 that are outside the lambda
                 */
                bView.getBall().setOnMouseEntered(e -> {
                    if (draggable) root.setCursor(Cursor.OPEN_HAND);
                });
                bView.getBall().setOnMouseExited(e -> {
                    if (draggable) root.setCursor(Cursor.DEFAULT);
                });
                bView.getBall().setOnMouseDragged(mouseEvent -> {
                    if (draggable) {
                        root.setCursor(Cursor.CLOSED_HAND);
                        gameController.getGameView().displayPoolCue(false);
                        gameController.getPoolCueController().getCueView().getPoolLine().setVisible(false);
                        gameController.getPoolCueController().setPoolCue(0, gameController.getPoolCueController().poolCueView.getPreviousAngle());
                        double newPositionX = (mouseEvent.getSceneX() - mouseAnchorX);
                        double newPositionY = (mouseEvent.getSceneY() - mouseAnchorY);
                        Circle newBallPosition = new Circle(newPositionX, newPositionY, BallModel.RADIUS);
                        Shape intersect = Shape.intersect(tView.getAccessibleArea(), newBallPosition);
                        if (intersect.getBoundsInLocal().getWidth() == -1) {
                            newPositionX = whiteBallView.getBall().getLayoutX();
                            newPositionY = whiteBallView.getBall().getLayoutY();
                        }
                        if (firstPlay && (newPositionX > gameController.getTableController().getTableView().getWhiteLine().getStartX() - BallModel.RADIUS + gameController.getTableController().getTableX())) {
                            newPositionX = gameController.getTableController().getTableView().getWhiteLine().getStartX() - BallModel.RADIUS + gameController.getTableController().getTableX();
                        }

                        for (BallModel ballModel : bModelList) {
                            if (!ballModel.equals(whiteBallModel)) {
                                double normalXComponent = ballModel.getPositionX().doubleValue() - newPositionX;
                                double normalYComponent = ballModel.getPositionY().doubleValue() - newPositionY;
                                double distance = Math.sqrt(Math.pow(normalXComponent, 2) + Math.pow(normalYComponent, 2));
                                if (distance < (2 * BallModel.RADIUS)) {
                                    newPositionX = whiteBallView.getBall().getLayoutX();
                                    newPositionY = whiteBallView.getBall().getLayoutY();
                                }
                            }
                        }
                        whiteBallModel.setPositionX(new BigDecimal(newPositionX));
                        whiteBallModel.setPositionY(new BigDecimal(newPositionY));
                    }
                });
                bView.getBall().setOnMouseReleased(e -> {
                    if (draggable) {
                        root.setCursor(Cursor.OPEN_HAND);
                        gameController.updatePoolCuePosition();
                    }
                });

            } else {
                stripeBViewList.add(bView);
            }

            /**
             * Adding the BallView to the Pane
             */
            root.getChildren().add(bView.getBall());
        }
        stripFull.addAll(stripeBModelList);
        solidFull.addAll(solidBModelList);

        bModelList.addAll(solidBModelList);
        bModelList.add(eightBallModel);
        bModelList.addAll(stripeBModelList);
        bModelList.add(whiteBallModel);

        bViewList.addAll(solidBViewList);
        bViewList.add(eightBallView);
        bViewList.addAll(stripeBViewList);
        bViewList.add(whiteBallView);
    }

    public void makeDraggable() {
        draggable = true;
    }

    public void makeUnDraggable() {
        draggable = false;
        gameController.getGameView().setCursor(Cursor.DEFAULT);
    }


    /**
     * Detects all the collisions and updates the ball position
     */
    public void detectCollision(ArrayList<BallModel> bModelList,ArrayList<Circle> holeList) {
        if(bModelList==null) bModelList=BallController.bModelList;
        if(holeList==null) holeList=this.holeList;

        detectCollisionWithOtherBalls(bModelList);
        detectCollisionWithTable(bModelList);
        for (BallModel bModel : bModelList) {
            if (!isMoving) {
                isMoving = bModel.isMoving;
            }
            bModel.updatePosition();
                BallView ballView = getBallViewFromBallModel(bModel);
                if (checkBallInHole(bModel,holeList)) {
                    bModel.setInHole(true);
                    whiteBallIn(ballView);
                }
            if(!gameController.getAiController().isAITraining())updateBallViewPosition(bModel);
        }
    }

    public static void updateBallViewPosition(BallModel bModel) {
        BallView bView = bViewList.get(bModelList.indexOf(bModel));
        bView.getBall().setLayoutX(bModel.getPositionX().doubleValue());
        bView.getBall().setLayoutY(bModel.getPositionY().doubleValue());
    }
     /**
     * Checks if the BallView is inside the hole
     * @param ballModel the ballmodel
     * @return <code>true</code> if the ball is inside the hole. <code>false</code> otherwise
     */
    private boolean checkBallInHole(BallModel ballModel,ArrayList<Circle> holes) {
        for(Circle hole:holes) {
            double xSquared = Math.pow((ballModel.getPositionX().doubleValue() - TableController.tableX - hole.getCenterX()), 2);
            double ySquared = Math.pow((ballModel.getPositionY().doubleValue() - TableController.tableY - hole.getCenterY()), 2);
            double centerToCenter = Math.sqrt(xSquared+ySquared);
            if(centerToCenter < hole.getRadius()) {
                if(!gameController.getAiController().isAITraining()) SoundController.BallInHole();
                return true;
            }
        }
        return false;
    }

    /**
     * Detects any collision between a ball and the table
     */
    private void detectCollisionWithTable(ArrayList<BallModel> bModelList) {
        Bounds intersectBounds;
        for (BallModel bModel : bModelList) {
            if (!bModel.isInHole()) {
                boolean overlapHappened;
                boolean fixedOverlap = false;
                while (!fixedOverlap) {
                    Circle ballShadow = new Circle(bModel.getPositionX().doubleValue(), bModel.getPositionY().doubleValue(), BallModel.RADIUS);
                    intersectBounds = Shape.intersect(TableBorderModel.tableBorderArea, ballShadow).getBoundsInLocal();
                    if ((intersectBounds.getWidth() != -1)) {
                        overlapHappened = true;
                        if (overlapHappened) TableBorderModel.applyReflection(bModel, gameController.getAiController().isAITraining());
                        double normalXComponent = (intersectBounds.getCenterX() - bModel.getPositionX().doubleValue());
                        double normalYComponent = (intersectBounds.getCenterY() - bModel.getPositionY().doubleValue());
                        double distance = Math.sqrt(Math.pow(normalXComponent, 2) + Math.pow(normalYComponent, 2));
                        double distanceX = ((normalXComponent * (((BallModel.RADIUS * 1.05) - distance) / distance)));
                        double distanceY = ((normalYComponent * (((BallModel.RADIUS * 1.05) - distance) / (distance))));
                        bModel.setPositionX(bModel.getPositionX().subtract(new BigDecimal(distanceX)));
                        bModel.setPositionY(bModel.getPositionY().subtract(new BigDecimal(distanceY)));
                    } else {
                        fixedOverlap = true;
                    }
                }
            }
        }
    }

    /**
     * Detects all the collisions between the balls
     */
    private void detectCollisionWithOtherBalls(ArrayList<BallModel> bModelList) {
        BallModel ballA, ballB;
        /**
         * Suppose we have the following list: [1,2,3,4]
         * We want to check just the following collisions:
         *          (1,2) , (1,3) , (1,4) , (2,3) , (2,4) , (3,4)
         * And we want to avoid checking same collision twice.
         * That is done using the double loop where i=0 and j=i+1
         * That will guaranty that we go over all the collisions without
         * going on the same collision twice and without checking collision
         * with the same ball.
         */
        for (int i = 0; i < bModelList.size(); i++) {
            for (int j = i + 1; j < bModelList.size(); j++) {
                ballA = bModelList.get(i);
                ballB = bModelList.get(j);
                if (!ballA.isInHole() && !ballB.isInHole()) {
                    if (ballA.handleMomentum(ballB)) {
                        if (firstCollide == null) {
                            if (ballA.equals(whiteBallModel)) {
                                firstCollide = ballB;
                            } else if (ballB.equals(whiteBallModel)) {
                                firstCollide = ballA;
                            }
                        }
                        //SoundController.BallsCollide();
                        if(!gameController.getAiController().isAITraining()) SoundController.BallsCollide();
                        isCollide = true;
                        if (firstCollide != null) {
                            if (firstCollide.equals(eightBallModel)) {
                                System.out.println("EightBall Touch");
                                foul = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes all the BallViews from the Pane then Clears the BallView ArrayList
     *
     * @param gameView The Pane that contains the BallViews
     */
    public void destroyViews(GameView gameView) {
        for (BallView bView : bViewList) {
            gameView.getChildren().remove(bView.getBall());
        }
        bViewList.clear();
        solidBViewList.clear();
        stripeBViewList.clear();
        System.out.println("Cleared All BallViews: " + bViewList.size());
    }

    /**
     * Clears the BallModels ArrayList
     */
    public void destroyModels() {
        bModelList.clear();
        solidBModelList.clear();
        stripeBModelList.clear();
        System.out.println("Cleared All BallModels: " + bModelList.size());
    }

    public ArrayList<Double> xballPos = new ArrayList<>();
    public ArrayList<Double> yballPos = new ArrayList<>();

    public void addBallsPosition() {
        for (BallView bv : bViewList) {
            xballPos.add(bv.getBall().getLayoutX());
            yballPos.add(bv.getBall().getLayoutY());
        }
    }

    public void ballInHole(BallModel ballModel, GameView gameView) {
        if (ballModelArrayList().contains(ballModel)) {
            ballModel.setVelocityX(PhysicsModule.ZERO);
            ballModel.setVelocityY(PhysicsModule.ZERO);
            int index = ballModelArrayList().indexOf(ballModel);
            BallView bView1 = getBallViewFromBallModel(ballModel);
            gameView.getChildren().remove(bView1.getBall());

            if (stripeBModelList.contains(ballModel)) {
                allStripeBallsIn(ballModel);
            }

            if (solidBModelList.contains(ballModel)) {
                allSolidBallsIn(ballModel);
            }
            ballModelArrayList().remove(ballModel);
            ballViewArrayList().remove(bView1);
            gameView.getBallsPrimaryPane().getChildren().remove(gameView.getBalls().get(index));
            gameView.getBallViews().remove(index);
            gameView.getBalls().remove(index);
            gameView.getSelectionCircles().remove(index);
            gameView.setClickedBallNumber(gameView.getClickedBallNumber() - 1);
            gameView.updateOnBallsClickedListener();
        }
    }

    public void allSolidBallsIn(BallModel bModel) {
        BallController.getSolidBModelList().remove(bModel);
        if (BallController.getSolidBModelList().isEmpty()) {
            System.out.println("All in for Solid");
            allInSolid = true;
        }
    }

    public void allStripeBallsIn(BallModel bModel) {
        BallController.getStripeBModelList().remove(bModel);
        if (BallController.getStripeBModelList().isEmpty()) {
            System.out.println("All in for Stripe");
            allInStripe = true;
        }
    }

    /**
     * RULES FOR THE GAME
     */

    public void turns(){
        makeUnDraggable();

        if(!firstPlay) {
            if (!isCollide) {
                System.out.println("No collide");
                foul = true;
            }
        }
        checkFoul();

        if(gameController.getGameType()==0) {
            foul=false;
            return;
        }

        //winnerPlayerPVP();
        System.out.println("Scored: "+scored);
        if (!scored || foul) {
            setCurrentPlayer();
            isCollide=false;
            //System.out.println("switch");
        }
        if(gameController.getGameType()>1){
            if(gameController.getP2().isTurn()){
                //aiController.train();
            }
        }

        //setting the pool cue on each turn
        poolCueController.getCueView().getCue().setImage(ResourcesLoader.poolCueImages.get(gameController.getCurrentPlayer().getSelectedPoolCue()-1));

        System.out.println(gameController.getCurrentPlayer().getBallType());
        foul=false;
        setFirstCollide(null);
        bModelInEachTurn.clear();
        System.out.println(gameController.getCurrentPlayer().getUsername() + "," + "your turn!");
        waitingForInput=true;
        counter++;

    }

    public void assignBallType(){

        if(counter > 0 && !setBallType){
            for(int i = 0;i<BallController.solidFull.size();i++){
                if(bModelInEachTurn.contains(BallController.solidFull.get(i))){
                    scored=true;
                    if(!gameController.getAiController().isAITraining()){
                        gameController.getCurrentPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                        gameController.getCurrentPlayer().getBallNeededIn().removeAll(BallController.stripeBModelList);
                        getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                        getNextPlayer().getBallNeededIn().removeAll(BallController.solidBModelList);
                        gameController.getCurrentPlayer().setBallType(0);
                        getNextPlayer().setBallType(1);
                        setBallType = true;
                        System.out.println(gameController.getCurrentPlayer().getUsername() + ": "+gameController.getCurrentPlayer().getBallType());
                        System.out.println(getNextPlayer().getUsername() + ": "+getNextPlayer().getBallType());
                    }
                }
            }
            for(int i = 0;i<BallController.stripFull.size();i++){
                if(bModelInEachTurn.contains(BallController.stripFull.get(i))){
                    scored = true;
                    if(!gameController.getAiController().isAITraining()) {
                        getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                        getNextPlayer().getBallNeededIn().removeAll(BallController.stripeBModelList);

                        gameController.getCurrentPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                        gameController.getCurrentPlayer().getBallNeededIn().removeAll(BallController.solidBModelList);
                        gameController.getCurrentPlayer().setBallType(1);
                        getNextPlayer().setBallType(0);
                        setBallType = true;

                        System.out.println(gameController.getCurrentPlayer().getUsername() + ": " + gameController.getCurrentPlayer().getBallType());
                        System.out.println(getNextPlayer().getUsername() + ": " + getNextPlayer().getBallType());
                    }
                    gameController.getCurrentPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                    gameController.getCurrentPlayer().getBallNeededIn().removeAll(BallController.solidBModelList);
                    gameController.getCurrentPlayer().setBallType(1);
                    getNextPlayer().setBallType(0);
                    setBallType = true;
                    scored=true;
                    gameController.getTableController().setBallGotInHole(true);
                    System.out.println(gameController.getCurrentPlayer().getUsername() + ": "+gameController.getCurrentPlayer().getBallType());
                    System.out.println(getNextPlayer().getUsername() + ": "+getNextPlayer().getBallType());
                }
            }
            if(!gameController.getAiController().isAITraining()) BallConfigurationDB.assignBallType(gameController.getGameType(),gameController.getP1().getBallType(),gameController.getP2().getBallType());
        }
    }
    public void checkFoul(){
        if(foul){
            System.out.println("Foul");
            makeDraggable();
        }
    }

    private void setCurrentPlayer(){
        gameController.getCurrentPlayer().setTurn(false);
        gameController.currentPlayer = getNextPlayer();
        gameController.getCurrentPlayer().setTurn(true);
    }
    private PlayerModel getNextPlayer(){
        if(gameController.getCurrentPlayer().equals(gameController.getP1())) return gameController.getP2();
        return gameController.getP1();
    }
    public void whiteBallIn(BallView ballView){
        BallModel bModel = BallController.getBallModelFromBallView(ballView);
        if(bModel.getNumber()==16){
            this.foul = true;
            bModel.setPositionX(new BigDecimal(gameController.getTableController().getTableView().getFullTable().getWidth()/2));
            bModel.setPositionY(new BigDecimal(gameController.getTableController().getTableView().getFullTable().getHeight()/2));
            bModel.setVelocityX(new BigDecimal(0.1));
            bModel.setVelocityY(new BigDecimal(0.1));
            System.out.println("whiteBall");
            bModel.setInHole(false);
        }else{
            if(!bModelInEachTurn.contains(bModel)) bModelInEachTurn.add(bModel);
        }

    }

    public void eightBallInIllegal(int gameType){
        if(bModelInEachTurn.contains(BallController.eightBallModel)) {
            if (!gameController.getGameView().getPopupWindow().isShowing() && gameType != 0){
                getNextPlayer().setScore(getNextPlayer().getScore() + 1);
            }
            if (gameType != 0) {
                gameController.getTableController().getTableView().getPlayersScore().setText(gameController.getP1().getScore() + " : " + gameController.getP2().getScore());
            }
            gameController.getGameView().getPopupMessage().setText(gameController.getCurrentPlayer() + " lose!");
            gameController.getGameView().getPopupWindow().show();
        }
    }
    public void eightBallInLegal(int gameType){
        if(bModelInEachTurn.contains(BallController.eightBallModel)) {
            if (!gameController.getGameView().getPopupWindow().isShowing() && gameType != 0){
                getNextPlayer().setScore(getNextPlayer().getScore() + 1);
            }
            if (gameType != 0) {
                gameController.getTableController().getTableView().getPlayersScore().setText(gameController.getP1().getScore() + " : " + gameController.getP2().getScore());
            }
            gameController.getGameView().getPopupMessage().setText(gameController.getCurrentPlayer() + " win!");
            gameController.getGameView().getPopupWindow().show();
        }
    }
    public void firstCollidePlay(){
        if(getFirstCollide() != null && setBallType) {
            if (gameController.getCurrentPlayer().isTurn() && !(getFirstCollide().getBallType() == gameController.getCurrentPlayer().getBallType())) {
                //System.out.println("Opposite type");
                foul = true;
            }
        }
    }

    public void ballInHole1(){
        if(!bModelInEachTurn.isEmpty()) {
            for (BallModel b : bModelInEachTurn) {
                scored = true;
                b.setVelocityX(BigDecimal.ZERO);
                b.setVelocityY(BigDecimal.ZERO);
                if(!gameController.getAiController().isAITraining()) {
                    ballInHole(b, gameController.getGameView());
                    if (gameController.getGameType() > 0) {
                        if (gameController.getP1().getBallNeededIn().contains(b)) {
                            gameController.getP1().getBallNeededIn().remove(b);
                        }
                        if (gameController.getP2().getBallNeededIn().contains(b)) {
                            gameController.getP2().getBallNeededIn().remove(b);
                        }
                    }
                    gameController.getTableController().setBallGotInHole(true);

                }
            }
            if (gameController.getTableController().getBallGotInHole()) {
                gameController.getTableController().getTableView().assignBallsInTableView(1, gameController.getGameView().getGameController().getP1().getBallNeededIn());
                gameController.getTableController().getTableView().assignBallsInTableView(2, gameController.getGameView().getGameController().getP2().getBallNeededIn());
                gameController.getTableController().setBallGotInHole(false);
            }

        }
    }



    public void winnerPlayerSolo(){
        if(gameController.getCurrentPlayer().getBallNeededIn().isEmpty()){
            gameController.getCurrentPlayer().getBallNeededIn().add(BallController.eightBallModel);
        }
        if(BallController.getAllInSolid() && BallController.getAllInStripe()){
            eightBallInLegal(0);
        }else{
            eightBallInIllegal(0);
        }
    }
    public void winnerPlayerPVP(){
        //System.out.println(currentPlayer+" : "+currentPlayer.getBallNeededIn());
        if(gameController.getCurrentPlayer().getBallNeededIn().isEmpty()){
            gameController.getCurrentPlayer().getBallNeededIn().add(BallController.eightBallModel);
        }
//        System.out.println(currentPlayer+" : "+currentPlayer.getBallNeededIn());
//        System.out.println(currentPlayer+" eight ball needed: "+currentPlayer.getBallNeededIn().contains(BallController.eightBallModel));
        if(!gameController.getAiController().isAITraining()) {
            if (gameController.getCurrentPlayer().isTurn() && gameController.getCurrentPlayer().getBallNeededIn().contains(BallController.eightBallModel)) {
                eightBallInLegal(1);
            } else {
                eightBallInIllegal(1);
            }
        }
    }


    /**
     * Gets all the BallViews
     *
     * @return the ArrayList that contains all the BallViews
     */
    public ArrayList<BallView> ballViewArrayList() {
        return bViewList;
    }

    /**
     * Gets all the BallModels
     *
     * @return the ArrayList that contains all the BallModels
     */
    public static ArrayList<BallModel> ballModelArrayList() {
        return bModelList;
    }

    public static ArrayList<BallView> getStripeBViewList() {
        return stripeBViewList;
    }

    public static ArrayList<BallView> getSolidBViewList() {
        return solidBViewList;
    }

    public static ArrayList<BallModel> getStripeBModelList() {
        return stripeBModelList;
    }

    public static ArrayList<BallModel> getSolidBModelList() {
        return solidBModelList;
    }

    public static Boolean getAllInStripe() {
        return allInStripe;
    }

    public static void setAllInStripe(Boolean allInStripe) {
        BallController.allInStripe = allInStripe;
    }

    public static Boolean getAllInSolid() {
        return allInSolid;
    }

    public static void setAllInSolid(Boolean allInSolid) {
        BallController.allInSolid = allInSolid;
    }

    public static BallModel getBallModelFromBallView(BallView bView) {
        return bModelList.get(bViewList.indexOf(bView));
    }

    public static BallView getBallViewFromBallModel(BallModel bModel) {
        return bViewList.get(bModelList.indexOf(bModel));
    }

    public static ArrayList<BallModel> getStripFull() {
        return stripFull;
    }

    public static ArrayList<BallModel> getSolidFull() {
        return solidFull;
    }

    public BallModel getFirstCollide() {
        return firstCollide;
    }

    public void setFirstCollide(BallModel firstCollide) {
        this.firstCollide = firstCollide;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public BallModel getBallModelFromNumber(int number) {
        for (BallModel bModel : bModelList) {
            if (bModel.getNumber() == number) {
                return bModel;
            }
        }
        return null;
    }

    public ArrayList<BallModel> getBModelInEachTurn() {
        return bModelInEachTurn;
    }

    public boolean isFoul() {
        return foul;
    }

    public void setFoul(boolean foul) {
        this.foul = foul;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    public void setWaitingForInput(boolean waitingForInput) {
        this.waitingForInput = waitingForInput;
    }

    public boolean isFirstPlay() {
        return firstPlay;
    }

    public void setFirstPlay(boolean firstPlay) {
        this.firstPlay = firstPlay;
    }

    public boolean isSetBallType() {
        return setBallType;
    }

    public void setSetBallType(boolean setBallType) {
        this.setBallType = setBallType;
    }
}
