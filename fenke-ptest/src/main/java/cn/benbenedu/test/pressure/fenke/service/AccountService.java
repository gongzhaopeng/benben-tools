package cn.benbenedu.test.pressure.fenke.service;

import cn.benbenedu.test.pressure.fenke.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountService {

    public List<Account> acquireAccounts(int n) {

        return List.of(
                new Account("1177@test.com", "123456"));
    }
}
