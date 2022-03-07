package report.principlesOfTheSystemsArchitecture;

public class 第二章場合分けのロジック整理 {
        /**
        * 原則
        * コードの塊はメソッドとして抽出して独立させる
        * 関連するデータとロジックは、1つのクラスにまとめる
        */

        /* 悪い例 */
        if(customerType.equals("child")) {
            fee = baseFee * 0.5;
        }

        /* 条件判定や計算をメソッドに抽出する */
        if(isChild()) {
            fee = childFee();
        }
        private boolean isChild() {
            return customerType.equals("child");
        }
        private int childFee() {
            return baseFee * 0.5;
        }

        /* 悪い例 条件分岐*/
        Yen Fee() {
            Yen result;
            if(isChild()) {
                result = childFee();
            } else if(isSenior()) {
                result = seniorFee();
            } else {
                result = adultFee();
            }
            return result;
        } // 条件間の依存関係がすごいよなあ？

        /* ガード節の利用 */
        Yen Fee() {
            if(isChild()) return childFee();
            if(isSenior()) return seniorFee();
            return adultFee();
        } // 依存関係が取り除かれたよなあ？

        Yen Fee() {
            if(isBaby()) return babyFee();// 条件の追加が楽だよなあ？
            if(isChild()) return childFee();
            if(isSenior()) return seniorFee();
            return adultFee();

        }

        /* step2 ロジックを別クラスに分ける */
        class AdultFee {
            Yen fee() {
                return new Yen(100);
            }
            String label() {
                return "大人";
            }
        }

        class ChildFee {
            Yen fee() {
                return new Yen(50);
            }
            String label() {
                return "子供";
            }
        }
        // クライアント側
        class Charge {
            Fee fee;
            public Charge(Fee fee) {
                this.fee = fee;
            }
            Yen yen() {
                return fee.yen();
            }
        }

        class Reservation {
            List<Fee> fees;

            Reservation() {
                fees = new ArrayList<Fee>();
            }

            void addFee(Fee fee) {
                fees.add(fee);
            }

            Yen feeTotal() {
                Yen total = new Yen();
                for(Fee each : fees) {
                    // Yenというデータ型を用いて加算メソッドを実装しているのかな？
                    // クライアント側から計算を隠蔽するのはいいなと思った
                    total.add(each.Yen());
                }
                return total;
            }
        }
        /**
         * Reservation はどんな料金が存在するのか知らねえよなあ？
         * 知っているのはFeeがYenメソッドを実装していることだけだよなあ
         * 知らない事が多いほど疎結合になるよなあ？
         */



        /* 区分ごとのインスタンスの生成方法
         *   区分ごとのインスタンスを生成するにはどの区分か判定する必要があるよなあ?
         *   その場合はif文を使って真偽値を判定しなければいけなくなるよなあ?
         *   そのif文すらも必要なくなるって!?
         */

        /* Mapによる解法 */
        class FeeFactory {
            static Map<String, Fee> types;

            // ここで区分を定義するよなあ？
            static
            {
                types = new HashMap<String, Fee>();
                types.put("adult", new AdultFee());
                types.put("child", new ChildFee());
            }

            // キーに定義した区分に紐づくインスタンスが返却されるよなあ？？？
            static Fee feeByName(String name) {
                return types.get(name);
            }
        }

        /* ええ!？ Enumをつかえばもっと簡単になるんですか！？！？！？ */

        /* enumの例 */
        enum FeeType {
            adult,
            child,
            senior
        }

        // クライアント
        class Guest {
            FeeType type;
            boolean isAdult() {
                return type.equals(Feetype.adult);
            }
        }
        /* 料金区分ごとのロジックをenumを使って表現する */
        enum FeeType {
            adult(new AdultFee()),
            child(new ChildFee()),
            senior(new SeniorFee());

            private Fee fee;

            private FeeType(Fee fee) {
                this.fee = fee;//料金区分ごとのオブジェクトを設定する
            }

            // enumで処理を移譲させるのが良いなと思った
            Yen yen() {
                return fee.yen();
            }

            String label() {
                return fee.label();
            }
        }

        // クライアント
        Yen feeFor(String feeTypeName) {
            // ここでインスタンスを設定する
            FeeType feeType = FeeType.valueOf(feeTypeName);
            // 移譲したメソッドを呼び出す
            return feeType.yen();
        }

        /* 状態遷移をenumで定義する */
        enum State {
            審査中,
            承認済,
            実施中,
            終了,
            差戻中,
            中断中
        }

        // 状態の一覧を取得
        State[] states = State.values();

        //状態をグルーピングする
        //EnumSet.of()でEnumに定義された値からSetを生成出来るらしい。すごいね
        Set nextStates = EnumSet.of(承認済, 差戻中);

        /* 任意の状態から遷移できるかを判定する */
        class StateTransitions {
            Map<State, Set<State>> allowed;
            {
                allowed = new HashMap<>();

                allowed.put(審査中, EnumSet.of(承認中, 差戻中));
                allowed.put(差戻中, EnumSet.of(審査中, 終了中));
                allowed.put(承認中, EnumSet.of(実施中, 終了));
                allowed.put(実施中, EnumSet.of(中断中, 終了));
                allowed.put(中断中, EnumSet.of(実施中, 終了));
            }
            boolean canTransit(State from, State to) {
                // 個々でキーに紐付いたSetを取得
                Set<State> allowedStates = allowed.get(from);
                // Setの中に次の状態が含まれているか確認
                return allowedStates.contains(to);
            }
        }
}
