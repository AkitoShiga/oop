class 第三章業務ロジックをわかりやすく説明する {
    /**
     * データとロジックを別のクラスに分けるとわかりにくい
     *
     *   データクラス -> データを保持するクラス -> バリューオブジェクト
     *
     *   業務アプリでもっとも多い -> 三層アーキテクチャ
     *     元はcやcobolで使われていたアーキテクチャ
     *     データクラスを三つのレイヤーでやりとりする
     *     データクラスを使用するロジックがどこにもかけてしまうよなあ？
     *       機能の変更があったときに同じような処理をしている箇所を調べないといけないよなあ？
     *       プレゼンテーション、アプリケーション、データソースが相互に依存するよなあ？
     *
     *       共通ライブラリをつかえばいいじゃない？？？
     *         全ての要件に合わせるとその分引数が増えるよなあ？
     *         極めて小さい関数をつくっても、関数の数が膨れ上がるよなあ
     *         だれも使わなくなるよなあ？
     *
     *   データとロジックを一体にするんや！！
     *
     *   データクラスはJava本来の使い方じゃないらしい
     *     Javaのクラス -> データとロジックを１つの単位にまとめる仕組み
     *     データとそこに紐づくロジックは同じクラスにまとめる
     *
     *     データとロジックをひとつに纏めておけばロジックは重複しないよなあ？
     *
     *     OOP的クラス設計
     *       メソッドにロジックを置く
     *       データクラスにロジックを記述する
     *       クライアント側にロジックを書かない
     *       メソッド単位を小さくしてロジックを移動しやすくする
     *       メソッドで必ずインスタンス変数をつかう
     *       クラスを小さく保つ
     *       クラスの整理はパッケージで行う
     */

    /* よくないメソッド */
    class Person {

        private String firstName;
        private String lastName;

        String getFirstName() {
            return firstName; // おい! こいつ自プロパティの値をそのまま返してるぞぉ！？
        }

        String getLastName() {
            return lastName; // おい! こいつ自プロパティの値をそのまま返してるぞぉ！？
        }
    }
    // 増田さんのここのデータクラスに関する解説が辛辣で笑った
    // 自分のプロパティの値のみを返すメソッドはアンチパターン
    // メソッド -> 判断/加工/計算をするもの

    /* よいメソッド */
    class PersonName {
        private String firstName;
        private Strig lastName;
        // メソッドにロジックをもたせるのが良いメソッド
        String fullName() {
            return String.format("%s%s", firstName, lastName);
        }
    }
    // データを持つクラスに業務ロジックを集めることがコードの重複や散財を防ぐ
    // データを持つクラスに業務ロジックを集約させることはOOPの基本
    // ロジックをどこに持たせるか考えるのが設計

    /* メソッドにインスタンスヘンスを使う */
    /* 良くない例 */
    BigDecimal total(BigDecimal unitPrice, BigDecimal quantity) {
        BigDecimal total = unitPrice.multiply(quantity);
        return total.setScale(0, ROUND_HALF_UP);
    }

    class Customer {

        String postalCode;
        String city;
        String address;

        String telephone;
        String mailAddress;
        boolean telephoneNotPreferred;

        // ここから下を外部に切り出したい
        String firstName;
        String lastName;
        String fullName() {
            return String.format("%s%s", firstName, lastName);
        }
        // これをこうじゃ
        class PersonName {
            private String firstName;
            private String lastName;

            String fullName() {
                return String.format("%s%s", firstName, lastName);
            }
        }
    }

    class Customer {
        PersonName personName;
        Address address;
        ContactMethod contactMethod;
    }

    /**
     * クラスを沢山作りすぎるとどこに何があるのかわからなるよなあ？？
     * ええ！？パッケージを使用すればそれが解決するんですか？？？
     *
     * パッケージ作成の観点
     * なるべくパッケージスコープで変数の宣言が出来るようにする
     *
     * パッケージによる整理は常に行っていく
     * */



}
