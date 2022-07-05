package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import net.bytebuddy.utility.RandomString;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    static int number;

    public static int numberAccount(List<Account> accounts){
        int accountNumber;
        String numberAccount;
        do{
            accountNumber = randomNum(99999999, 10000000); // 10000000
            numberAccount = "VIN" + accountNumber;
        }while(accounts.stream().map(account -> account.getNumber()).collect(Collectors.toList()).contains(numberAccount));

        return accountNumber;
    }
    public static int randomNum(int max, int min) {

        return number = (int) ((Math.random() * (max - min)) + min);
    }

    public static String generateToken() {
        String token = RandomString.make(38);

        return token;

    }
}
