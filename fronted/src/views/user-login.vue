<template>
    <div class="login-wrap">
        <el-form class="login-container" :rules="rules" :model="form" ref="form">
            <!-- 标题 -->
            <h1 class="title">Aw Chat</h1>
            <!-- 账号 -->
            <el-form-item label="ID/邮箱" prop="account">
                <el-input type="text" v-model="form.account" placeholder="ID/邮箱"></el-input>
            </el-form-item>
            <!-- 密码 -->
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="form.password" placeholder="密码"></el-input>
            </el-form-item>
            <!-- 记住密码开关 -->
            <el-form-item>
                <el-switch v-model="checked" active-text="记住密码" />
            </el-form-item>
            <!-- 提交按钮 -->
            <el-form-item>
                <el-button type="primary" style="width: 100%;" @click="dosubmit()">登录</el-button>
            </el-form-item>
            <el-form-item style="text-align: right;">
                <p>还没有账号，去<router-link to='/reg'>注册</router-link></p>
            </el-form-item>
        </el-form>
    </div>
</template>
   
<script>
import api from "../api/api";
import util from "@/api/util";
export default {
    name: 'user-login',
    data() {
        return {
            form: {
                account: "",
                password: ""
            },
            checked: false,
            rules: {
                account: [
                    { required: true, message: '用户ID/邮箱', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (/\d{1,11}/.test(value) == false &&
                                /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/.test(value) == false) {
                                callback(new Error('ID或邮箱输入有误'))
                            } else {
                                callback()
                            }
                        },
                        trigger: 'blur'
                    }
                ],
                password: [
                    { required: true, message: '用户密码', trigger: 'blur' },
                ],
            },
        }
    },
    methods: {
        jumphome: function () {
            // 保存用户名密码
            if (this.checked) {
                util.saveAccount(this.form.account)
                util.savePassword(this.form.password)
            } else {
                util.delAccount()
                util.delPassword()
            }
            // 跳转路由
            this.$router.push({
                name: 'home',
                params: { account: this.form.account }
            })
        },
        dosubmit: function () {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    api.user.verifyUser(this.form.account, this.form.password).then((res) => {
                        var r = res.data
                        if (r == 0) {
                            this.$message.error('密码错误')
                        } else if (r == -1) {
                            this.$message.error('ID或邮箱不存在')
                        } else {
                            this.jumphome()
                        }
                    }).catch(function (error) {
                        console.log(error)
                    })
                }
            })
        }
    },
    mounted() {
        if (util.loadAccount()) {
            this.form.account = util.loadAccount()
            this.form.password = util.loadPassword()
            this.checked = true;
        }
    }
}
</script>
   
<style lang="scss">
#app {
    .login-wrap {
        box-sizing: border-box;
        width: 100%;
        height: 100%;
        padding-top: 10%;

        .login-container {
            border-radius: 10px;
            margin: 0px auto;
            width: 350px;
            padding: 30px 35px 15px 35px;
            background: #fff;
            border: 1px solid #eaeaea;
            text-align: left;
            box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);

            .title {
                margin: 0px auto 40px auto;
                text-align: center;
                color: #505458;
            }
        }
    }
}
</style>