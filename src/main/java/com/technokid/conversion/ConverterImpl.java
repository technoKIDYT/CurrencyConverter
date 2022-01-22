package com.technokid.conversion;

import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.technokid.model.Output;
import com.technokid.model.TKCurrency;

public class ConverterImpl implements Converter {

	private WebClient webClient;
	private HtmlPage page;

	@Override
	public Output convert(TKCurrency from, TKCurrency to) {
		String url = "https://www.xe.com/currencyconverter/convert/?Amount=" + from.getAmount() + "&From="
				+ from.getUnit() + "&To=" + to.getUnit();
		HtmlElement element = null;
		HtmlElement element2 = null;
		Output output = null;
		System.out.println(url);
		String outXpath = "//p[@class='result__BigRate-sc-1bsijpp-1 iGrAod']";
		String exchangeRateXpath = "//div[@class='unit-rates___StyledDiv-sc-1dk593y-0 dEqdnx']";
		try {
			webClient = new WebClient();
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(url);
			output = new Output();
			element = (HtmlElement) page.getByXPath(outXpath).get(0);
			output.setOutput(element.getVisibleText());
			element2 = (HtmlElement) page.getByXPath(exchangeRateXpath).get(0);
			output.setExchangeRate(element2.getVisibleText());
			System.out.println(element.getVisibleText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output != null ? output : null;
	}

	@Override
	public Set<TKCurrency> getAllCurrencies() {
		Set<TKCurrency> toret = new HashSet<TKCurrency>();
		Locale[] locs = Locale.getAvailableLocales();
		for (Locale loc : locs) {
			try {
				Currency currency = Currency.getInstance(loc);
				if (currency != null) {
					TKCurrency tkCurrency = new TKCurrency();
					tkCurrency.setUnit(currency.getCurrencyCode());
					tkCurrency.setStrDesc(currency.getDisplayName());
					toret.add(tkCurrency);
				}
			} catch (Exception exc) {

			}
		}
		return toret;
	}
}
