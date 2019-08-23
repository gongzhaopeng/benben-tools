package cn.benbenedu.test.pressure.fenke.service;

import cn.benbenedu.test.pressure.fenke.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class AccountService {

    public List<Account> acquireAccounts(int n) {

        return IntStream.range(1102, 1181).mapToObj(i ->
                new Account(i + "@test.com", "123456"))
                .collect(Collectors.toList());
    }
}
