export class AppSettings {
    public static API_ENDPOINT = 'http://localhost:5001/api';
    public static STRIP_PUBLIC_KEY = 'pk_test_6pRNASCoBOKtIshFeQd4XMUh';
    //pk_test_6pRNASCoBOKtIshFeQd4XMUh
    public static DEFAULT_CURRENCY = 'eur';
    public static DEFAULT_COUNTRY = 'DE';
    public static MAX_POLL_COUNT = 10;

    public static PAYMENT_INTENT_SOURCES: string[] = ['card'];
}
