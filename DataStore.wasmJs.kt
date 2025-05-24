package ru.ssshteam.potatocoder228.messenger


@JsFun( """(key, date, daysToLive = 7) => {
        const dataString = JSON.stringify(data);
        const encodedData = encodeURIComponent(dataString);
        const date = new Date();
        date.setTime(date.getTime() + (daysToLive * 24 * 60 * 60 * 1000));
        const expires = `expires=`${'$'}{date.toUTCString()}`;
        document.cookie = `${'$'}{key}=${'$'}{encodedData}; ${'$'}{expires}; path=/; SameSite=None; Secure;`;
        }""")
external fun saveCookie(
    key: String,
    data: String,
    daysToLive: Int = definedExternally
)

@JsFun("""
    (key, date, daysToLive = 7) => {
    const cookies = document.cookie.split('; ');
    const cookie = cookies.find(c => c.startsWith(`${'$'}{key}=`));

    if (!cookie) return defaultValue;

    const encodedData = cookie.split('=')[1];
    const dataString = decodeURIComponent(encodedData);

    return JSON.parse(dataString);
    }
""")
external fun getCookie(key:String, defaultValue:String = definedExternally):String

@JsFun("""
     (key) => {
     document.cookie = `${'$'}{key}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
""")
external fun clearCookie(key:String):String

actual class DataStore {

    actual fun setToken(token:String){
        saveCookie("token", token)
    }
    actual fun getToken():String{
        return getCookie("token")
    }
}