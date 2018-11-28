package com.staffgenics.training.banking.nbp;

import com.staffgenics.training.banking.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Service
public class NbpService {

  private static final String URI_GET_GOLD_EXCHANGE_RATE = "http://api.nbp.pl/api/cenyzlota/{date}?format=json";

  private static final String URI_GET_CURRENCY_RATES = "http://api.nbp.pl/api/exchangerates/tables/a/{date}/";

  private final RestTemplate rest;

  @Autowired
  public NbpService(RestTemplate rest) {
    this.rest = rest;
  }

  BigDecimal getGoldExchangeRate(String date){
    GoldExchangeRateDto[] rates = rest.getForObject(URI_GET_GOLD_EXCHANGE_RATE,GoldExchangeRateDto[].class, date);
    if (rates.length <= 0){
      throw new NotFoundException("Brak kursu złota dla dnia: " + date);
    }
    return rates[0].getCena();
  }

  Object[] getExchangeRatesForCurrencies(String date){
    Object[] currencies = rest.getForObject(URI_GET_CURRENCY_RATES,Object[].class, date);
    log.info(currencies[0].toString());
    if (currencies.length <= 0){
      throw new NotFoundException("Brak kursow walut dla danego dnia");
    }
    return currencies;
  }

  @Scheduled(fixedRate = 2000)
  void runSchedulerForCurrencies(){
    Object[] currencies = rest.getForObject(URI_GET_CURRENCY_RATES,Object[].class, "2018-11-27");
    log.info(currencies[0].toString());
  }
}
