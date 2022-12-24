import { Base64 } from "js-base64"

export default {
    dateFormat(fmt, date) {
        let ret;
        const opt = {
            "Y+": date.getFullYear().toString(),        // 年
            "m+": (date.getMonth() + 1).toString(),     // 月
            "d+": date.getDate().toString(),            // 日
            "H+": date.getHours().toString(),           // 时
            "M+": date.getMinutes().toString(),         // 分
            "S+": date.getSeconds().toString()          // 秒
            // 有其他格式化字符需求可以继续添加，必须转化成字符串
        };
        for (let k in opt) {
            ret = new RegExp("(" + k + ")").exec(fmt);
            if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
            }
        }
        return fmt;
    },
    getLogoSrc() {
        return "http://localhost:8088/api/files/logo.png"
    },
    getUserAvatarSrc(id) {
        try {
            return "http://localhost:8088/api/files/user_" + id +".png"
        } catch (e) {
            return "http://localhost:8088/api/files/logo.png"
        }
    },
    getGroupAvatarSrc(id) {
        try {
            return "http://localhost:8088/api/files/group_" + id +".png"
        } catch (e) {
            return "http://localhost:8088/api/files/logo.png"
        }
    },
    saveId(id) {
        localStorage.setItem("id", id)
    },
    loadId() {
        return localStorage.getItem("id")
    },
    loadAccount() {
        return localStorage.getItem("account")
    },
    saveAccount(account) {
        localStorage.setItem("account", account)
    },
    delAccount() {
        localStorage.removeItem("account")
    },
    loadPassword() {
        return Base64.decode(localStorage.getItem("password"))
    },
    savePassword(password) {
        localStorage.setItem("password", Base64.encode(password))
    },
    delPassword() {
        localStorage.removeItem("password")
    },
}