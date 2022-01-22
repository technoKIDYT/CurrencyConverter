module com.technokid.CurrencyConverter {
    requires javafx.controls;
    requires javafx.fxml;
	requires htmlunit;
	requires neko.htmlunit;

    opens com.technokid.CurrencyConverter to javafx.fxml;
    exports com.technokid.CurrencyConverter;
}
