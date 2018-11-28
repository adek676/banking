package com.staffgenics.training.banking.nbp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class NbpController {

  private final NbpService nbpService;

  @Autowired
  public NbpController(NbpService nbpService) {
    this.nbpService = nbpService;
  }

  @RequestMapping(value = "/nbp/gold-exchange/{date}", method = RequestMethod.GET)
  public BigDecimal getGoldRate(@PathVariable String date){
    return nbpService.getGoldExchangeRate(date);
  }

  @RequestMapping(value = "/nbp/currency-rates/{date}")
  public Object[] getCurrenciesForDate(@PathVariable String date){
    return nbpService.getExchangeRatesForCurrencies(date);
  }
}
