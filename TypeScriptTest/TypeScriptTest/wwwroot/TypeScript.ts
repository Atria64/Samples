namespace Test {
    class Functions {
        //与えられた文字列をalertで表示します
        public showAlert(message: string){
            alert(message);
        }
        //与えられた文字列を編集して返します。
        public editText(message: string): string {
            return message+" : edited";
        }
    }
    export function Load(): void {
        window['Functions'] = new Functions();
    }
}

Test.Load();