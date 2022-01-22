package com.technokid.CurrencyConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import com.technokid.conversion.ConverterImpl;
import com.technokid.model.Output;
import com.technokid.model.TKCurrency;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Controller implements Initializable {

	@FXML
	private Button btnConvert;

	@FXML
	private ComboBox<TKCurrency> drpFrom;

	@FXML
	private ComboBox<TKCurrency> drpTo;

	@FXML
	private TextField inpAmount;

	@FXML
	private VBox outVbox;

	@FXML
	private Label fromOut;

	@FXML
	private Label toOut;

	@FXML
	private Button btnSwap;

	@FXML
	private Text exchRate;

	@FXML
	private ImageView loader;

	@FXML
	private AnchorPane root;

	private ConverterImpl converterImpl;
	private Output outPut;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		converterImpl = new ConverterImpl();
		Set<TKCurrency> currList = converterImpl.getAllCurrencies();
		drpFrom.getItems().addAll(currList);
		drpTo.getItems().addAll(currList);
		outVbox.setVisible(false);
		loader.setVisible(false);
		root.setDisable(false);
	}

	@SuppressWarnings("exports")
	public void convert(ActionEvent actionEvent) {
		boolean isAmountOk = false, isFromCurrOk = false, isToCurrOk = false;

		if (isValidToCurr()) {
			isAmountOk = true;
		} else {
			isAmountOk = false;
		}

		if (isValidFromCurr()) {
			isFromCurrOk = true;
		} else {
			isFromCurrOk = false;
		}

		if (isValidAmountData()) {
			isToCurrOk = true;
		} else {
			isToCurrOk = false;
		}

		if (isAmountOk && isFromCurrOk && isToCurrOk) {
			loader.setVisible(true);
			outVbox.setVisible(false);
			root.setDisable(true);

			if (actionEvent.getSource().equals(btnSwap)) {
				Duration duration = Duration.millis(300);
				RotateTransition rotateTransition = new RotateTransition(duration, btnSwap);
				rotateTransition.setByAngle(180);
				rotateTransition.play();

				ObservableList<TKCurrency> from = drpFrom.getItems();
				ObservableList<TKCurrency> to = drpTo.getItems();

				int fromIndex = drpFrom.getSelectionModel().getSelectedIndex();
				int toIndex = drpTo.getSelectionModel().getSelectedIndex();

				drpTo.getItems().clear();
				drpTo.getItems().addAll(from);
				drpTo.getSelectionModel().select(fromIndex);

				drpFrom.getItems().clear();
				drpFrom.getItems().addAll(to);
				drpFrom.getSelectionModel().select(toIndex);

				TKCurrency currencyFrom = new TKCurrency();
				currencyFrom.setAmount(Double.valueOf(inpAmount.getText()));
				currencyFrom.setUnit(drpFrom.getSelectionModel().getSelectedItem().getUnit());
				currencyFrom.setStrDesc(drpFrom.getSelectionModel().getSelectedItem().getDisplayName());
				TKCurrency currencyTo = new TKCurrency();
				currencyTo.setUnit(drpTo.getSelectionModel().getSelectedItem().getUnit());

				Thread thread = new Thread(() -> {
					outPut = converterImpl.convert(currencyFrom, currencyTo);
					Platform.runLater(() -> {
						outVbox.setVisible(true);
						loader.setVisible(false);
						root.setDisable(false);
						fromOut.setText(currencyFrom.getAmount() + " " + currencyFrom.getDisplayName() + " = ");
						toOut.setText(outPut.getOutput());
						exchRate.setText(outPut.getExchangeRate());
					});
				});
				thread.start();
			} else {
				TKCurrency currencyFrom = new TKCurrency();
				currencyFrom.setAmount(Double.valueOf(inpAmount.getText()));
				currencyFrom.setUnit(drpFrom.getSelectionModel().getSelectedItem().getUnit());
				currencyFrom.setStrDesc(drpFrom.getSelectionModel().getSelectedItem().getDisplayName());
				TKCurrency currencyTo = new TKCurrency();
				currencyTo.setUnit(drpTo.getSelectionModel().getSelectedItem().getUnit());
				
				Thread thread = new Thread(() -> {
					outPut = converterImpl.convert(currencyFrom, currencyTo);
					Platform.runLater(() -> {
						outVbox.setVisible(true);
						loader.setVisible(false);
						root.setDisable(false);
						fromOut.setText(currencyFrom.getAmount() + " " + currencyFrom.getDisplayName() + " = ");
						toOut.setText(outPut.getOutput());
						exchRate.setText(outPut.getExchangeRate());
					});
				});
				thread.start();
			}

		}
	}

	public boolean isValidAmountData() {
		boolean isValid = false;
		if (!inpAmount.getText().equals("")) {
			try {
				Double.parseDouble(inpAmount.getText());
				isValid = true;
			} catch (Exception e) {
				inpAmount.requestFocus();
				isValid = false;
			}
		} else {
			inpAmount.requestFocus();
			isValid = false;
		}
		return isValid;
	}

	public boolean isValidFromCurr() {
		boolean isValid = false;
		if (drpFrom.getSelectionModel().getSelectedItem() != null) {
			isValid = true;
		} else {
			drpFrom.requestFocus();
		}
		return isValid;
	}

	public boolean isValidToCurr() {
		boolean isValid = false;
		if (drpTo.getSelectionModel().getSelectedItem() != null) {
			isValid = true;
		} else {
			drpTo.requestFocus();
		}
		return isValid;
	}
}
