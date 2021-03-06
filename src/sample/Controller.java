package sample;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Author; // DELETE THESE WHEN DONE *************************************************************************************
import model.AuthorDAO;
import javafx.scene.control.TextArea;

import model.CharacterEntity;
import model.CharacterEntityDAO;
import util.DBUtil;

public class Controller extends Main implements Initializable {

  @FXML
  private TextArea characterText;
  @FXML
  public TableView<CharacterEntity> CharacterEntityTableView;
  @FXML
  private TableColumn<CharacterEntity, Integer> playerID;
  @FXML
  private TableColumn<CharacterEntity, String> charName;
  @FXML
  private TableColumn<CharacterEntity, String> charClass;
  @FXML
  public TextArea textArea;


  //Populate Author
  @FXML
  private void populateCharacterEntity(CharacterEntity characterEntity)
      throws ClassNotFoundException {
    //Declare an ObservableList for table view
    ObservableList<CharacterEntity> characterEntityData = FXCollections.observableArrayList();
    //Add employee to the ObservableList
    characterEntityData.add(characterEntity);
    //Set items to the employeeTable
    CharacterEntityTableView.setItems(characterEntityData);
  }

  //Populate Authors for TableView
  @FXML
  private void populateCharacterEntities(ActionEvent actionEvent)
      throws ClassNotFoundException, SQLException {
    //Set items to the employeeTable
    try {
      ResultSet rsCharacterEntity = DBUtil
          .dbExecuteQuery("SELECT * FROM CHARACTERS ORDER BY playerID");
      ObservableList<CharacterEntity> characterList = CharacterEntityDAO
          .getCharacterEntityList(rsCharacterEntity);
      CharacterEntityTableView.setItems(characterList);
    } finally {

    }
  }


  //Insert a character entity to the DB
  @FXML
  private void insertCharacterEntity(ActionEvent actionEvent)
      throws SQLException, ClassNotFoundException {
    try {
      CharacterEntityDAO
          .insertCharacter(playerID.getText(), charName.getText(), charClass.getText());
      textArea.setText("Character inserted! \n");
      searchCharacterEntities(new ActionEvent());
    } catch (SQLException e) {
      // resultArea.setText("Problem occurred while inserting author " + e);
      throw e;
    }
  }

  //Search a Character Entity
  @FXML
  private void searchCharacterEntity(ActionEvent actionEvent)
      throws ClassNotFoundException, SQLException {
    try {
      //Get Author information
      CharacterEntity characterEntity = CharacterEntityDAO
          .searchCharacterEntity(playerID.getText());
      //Populate Author on TableView and Display on TextArea
      populateAndShowCharacterEntity(characterEntity);
    } catch (SQLException e) {
      e.printStackTrace();
      textArea.setText("Error occurred while getting character entity information from DB.\n" + e);
      throw e;
    }
  }

  @FXML
  private void searchCharacterEntities(ActionEvent actionEvent)
      throws SQLException, ClassNotFoundException {
    try {
      //Get all Character Entity information
      ObservableList<CharacterEntity> characterData = CharacterEntityDAO.searchCharacterEntities();
      //Populate Character Entities on TableView
      // populateCharacterEntities(characterData);
    } catch (SQLException e) {
      System.out.println("Error occurred while getting authors information from DB.\n" + e);
      throw e;
    }
  }

  //Populate Character for TableView and Display Character on TextArea
  @FXML
  private void populateAndShowCharacterEntity(CharacterEntity characterEntity)
      throws ClassNotFoundException {
    if (characterEntity != null) {
      populateCharacterEntity(characterEntity);
      setCharacterEntityInfoToTextArea(characterEntity);
    } else {
      textArea.setText("This character does not exist!\n");
    }
  }

  //Set Author information to Text Area
  @FXML
  private void setCharacterEntityInfoToTextArea(CharacterEntity characterEntity) {
    textArea.setText("playerID: " + characterEntity.getPlayerID() + "\n" +
        "Character Name: " + characterEntity.getCharacterName() + "\n" +
        "Class: " + characterEntity.getClassName());
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {

    playerID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
    charName.setCellValueFactory(cellData -> cellData.getValue().characterNameProperty());
    charClass.setCellValueFactory(cellData -> cellData.getValue().classProperty());

    //CharacterEntityTableView.getColumns().addAll(playerID,charName,charClass);
  }

  //Delete all CharacterEntities with a given playerId from DB
  @FXML
  private void deleteCharacterEntities(ActionEvent actionEvent)
      throws SQLException, ClassNotFoundException {
    try {
      CharacterEntityDAO.deleteCharacterEntityWithId(characterText.getText());
      textArea.setText("Characters deleted! Player id: " + characterText.getText() + "\n");
      searchCharacterEntities(new ActionEvent());
    } catch (SQLException e) {
      textArea.setText(
          "Enter the id of the player to delete. \nProblem occurred while deleting player " + e);
      throw e;
    }
  }
}
