import Axios from 'axios'

Axios.defaults.baseURL = 'http://localhost:8088/api/'

export default {
    user: {
        verifyUser(account, password) {
            var params = {
                account: account,
                password: password
            }
            return new Promise((resolve, reject) => {
                Axios.get('/user/verify', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        insertUser(username, password, sex, birth, email, info) {
            var params = {
                username: username,
                password: password,
                sex: sex,
                birth: birth,
                email: email,
                info: info
            }
            return new Promise((resolve, reject) => {
                Axios.post('user/insert', params).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        updateUser(id, username, password, sex, birth, email, info) {
            var params = {
                id: id,
                username: username,
                password: password,
                sex: sex,
                birth: birth,
                email: email,
                info: info
            }
            return new Promise((resolve, reject) => {
                Axios.post('user/update', params).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        searchUser(idOrName) {
            var params = {
                name: idOrName
            }
            return new Promise((resolve, reject) => {
                Axios.get('/user/search', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        addFriend(me, friend) {
            var params = {
                me: me,
                friend: friend
            }
            return new Promise((resolve, reject) => {
                Axios.get('/friend/add', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        delFriend(me, friend) {
            var params = {
                me: me,
                friend: friend
            }
            return new Promise((resolve, reject) => {
                Axios.get('/friend/delete', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        acceptFriend(me, friend) {
            var params = {
                me: me,
                friend: friend
            }
            return new Promise((resolve, reject) => {
                Axios.get('/friend/accept', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        refuseFriend(me, friend) {
            var params = {
                me: me,
                friend: friend
            }
            return new Promise((resolve, reject) => {
                Axios.get('/friend/refuse', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        getFriendHistoryMessage(me, friend, id) {
            var params = {
                me: me,
                friend: friend,
                id: id
            }
            return new Promise((resolve, reject) => {
                Axios.get('/message/gf', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        getGroupHistoryMessage(group, id) {
            var params = {
                group: group,
                id: id
            }
            return new Promise((resolve, reject) => {
                Axios.get('/message/gg', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        sendMessage(message) {
            return new Promise((resolve, reject) => {
                Axios.post('/message/send', message).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        readMessage(messageId, userId) {
            var params = {
                id: messageId,
                user: userId
            }
            return new Promise((resolve, reject) => {
                Axios.get('/message/read', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        searchGroup(group) {
            var params = {
                group: group
            }
            return new Promise((resolve, reject) => {
                Axios.get('/group/search', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        enterGroup(group, user) {
            var params = {
                group: group,
                user: user
            }
            return new Promise((resolve, reject) => {
                Axios.get('/group/enter', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        exitGroup(group, user) {
            var params = {
                group: group,
                user: user
            }
            return new Promise((resolve, reject) => {
                Axios.get('/group/exit', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        login(account) {
            var params = {
                account: account
            }
            return new Promise((resolve, reject) => {
                Axios.get('/login', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
        logout(id) {
            var params = {
                id: id
            }
            return new Promise((resolve, reject) => {
                Axios.get('/logout', {params}).then(res => {
                    resolve(res)
                }).catch(res => {
                    reject(res)
                })
            })
        },
    }
}