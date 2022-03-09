class 第五章アプリケーション機能を組み立てる {
    // 口座からの預金引き出しサービス
    @Service
    class BankAccountService {
        @Autowired
        BankAccountRepository repository;
        // おいこいつ引き出しと参照を同時にやってるぞ！？
        Amount withdraw(Amount amount) {
            repository.withdraw(amount);
            return repository.balance();
        }
    }
    // 参照
    @Service
    class BankAccountService {
        @Autowired
        BankAccountRepository repository;

        Amount balance() {
            return repository.balance();
        }

        boolean canWithdraw(Amount amount) {
            Amount balance = balance();
            return balance.has(amount);
        }
    }
    // 更新
    class BankAccountUpdateService {
        @Autowired
        BankAccountRepository repository;

        void withdraw(Amount amount) {
            repository.withdraw(amount);
        }
    }

    // 組み立て用のサービスクラス（シナリオクラス）にまとめる
    @Service
    class BankAccountScenario {
        @Autowired
        BankAccountService queryService;
        @Autowired
        BankAccountUpdateService = updateService;

        Amount withdraw(Amount amount) {
            if(! queryService.canWithdraw(amount)) {
                if(!queryService.canWithdraw(amount))
                    throw new IllegalStateException("残高不足");
                updateService.withdraw(amount);
                return queryService.balance();
            }
        }
    }
}
