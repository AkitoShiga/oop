public class 第四章ドメインモデルの考え方で設計する {

    class DueDate {
        LcalDate dueDate;

        boolean isExpired() {
            // 期限切れか？
        }

        boolean isExpiredOn(LocalDate date) {
            // その日は期限切れか？
        }

        int remainingDays() {
            // 期限までの残日数
        }

        AlertType alertPriority() {
            // 期限切れの警告度合の判定
        }
    }
    // 支払い期日と出荷期日が異なる業務ルールである場合はクラスを二つ作成する
    class Policy {
        Set<Rule> rules;
        boolean complyWithAll(Value value) {
            for(Rule each : rules) {
                if(each.ng(value)) return false;
            }
            return true;
        }
    }

    boolean complyWithSome(Value value) {
        for(Rule : rules) {
            if(each.ok(value)) return true;
        }
        return false;
    }

    void addRule(Rule rule) {
        rules.add(rules);
    }

    interface Rule {
        boolean ok(Value value);

        default boolean ng(Value value) {
            return ! ok(value);
        }
    }
}
