<template>
    <div class="reg-wrap">
        <el-form ref="form" :model="form" :rules="rules" class="reg-container">
            <h1 class="title">Aw Chat 用户注册</h1>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email"></el-input>
            </el-form-item>
            <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="password2">
                <el-input type="password" v-model="form.password2"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
                <el-radio-group v-model="form.sex">
                    <el-radio label="男"></el-radio>
                    <el-radio label="女"></el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期" prop="mail">
                <el-date-picker type="date" placeholder="选择出生日期" v-model="form.birth"
                    style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="个性签名" prop="info">
                <el-input type="text" v-model="form.info" placeholder="个性签名"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" style="width: 100%;" @click="dosubmit()">注册</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import api from "../api/api";
export default {
    name: 'user-reg',
    data() {
        var validatePass2 = (rule, value, callback) => {
            if (value === "") {
                callback(new Error("请再次输入密码"));
            } else if (value !== this.form.password) {
                callback(new Error("两次输入密码不一致!"));
            } else {
                callback();
            }
        };
        return {
            validatePass2,
            form: {
                email: '',
                username: '',
                password: '',
                password2: '',
                sex: '',
                birth: '',
                info: ''
            },
            rules: {
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/.test(value) == false) {
                                callback(new Error('邮箱格式错误'))
                            } else {
                                api.user.verifyUser(value, 0).then((res) => {
                                    var r = res.data
                                    if (r != -1) {
                                        callback(new Error('邮箱已经被注册'))
                                    } else {
                                        callback()
                                    }
                                }).catch(function (error) {
                                    console.log(error);
                                })
                            }
                        },
                        trigger: 'blur'
                    }
                ],
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                password2: [
                    { required: true, validator: validatePass2, trigger: 'blur' },
                ]
            }
        }
    },
    methods: {
        dosubmit() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    api.user.insertUser(this.form.username,
                        this.form.password,
                        this.form.sex,
                        this.form.birth,
                        this.form.email,
                        this.form.info).then(res => {
                            this.$message({
                                type: 'success',
                                message: "注册成功，你的用户ID为" + res.data
                            });
                            this.$router.replace('/')
                        })
                }
            })
        }
    }
}

</script>

<style lang="scss">
#app {
    .reg-wrap {
        box-sizing: border-box;
        width: 100%;
        height: 100%;
        padding-top: 1%;

        .reg-container {
            border-radius: 10px;
            margin: 0px auto;
            width: 800px;
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