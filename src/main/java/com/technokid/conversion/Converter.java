package com.technokid.conversion;

import java.util.Set;

import com.technokid.model.Output;
import com.technokid.model.TKCurrency;

public interface Converter {
	Output convert(TKCurrency from, TKCurrency to);

	Set<TKCurrency> getAllCurrencies();
}
