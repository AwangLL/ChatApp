<template>
    <div class="home-wrap">
        <el-container>
            <el-aside width="80px">
                <!-- 头像 -->
                <el-row>
                    <img class="avatar" :src="this.avatar.src" @click="updateUserInfo()"
                        alt="http://localhost:8088/api/files/logo.png" />
                </el-row>
                <!-- 用户名 -->
                <el-row>
                    <p style="color: white;">{{ data.user.username }}</p>
                </el-row>
                <el-row style="margin-top:50px">
                    <el-button icon="el-icon-chat-round" @click="switchMenu(1)"></el-button>
                </el-row>
                <el-row>
                    <el-button icon="el-icon-user" @click="switchMenu(2)"></el-button>
                </el-row>
                <el-row>
                    <el-button icon="el-icon-s-comment" @click="switchMenu(3)"></el-button>
                </el-row>
            </el-aside>
            <el-main>
                <KeepAlive>
                    <chat-menu ref="chatmenu" v-if="this.menuIndex == 1" :ready_message="this.data.messages"
                        :friends="this.data.friends" :groups="this.data.groups" :id="this.data.user.id" />
                    <friend-menu v-if="this.menuIndex == 2" :id="this.data.user.id" :friends="this.data.friends"
                        :newfriends="this.data.newfriends" @delFriend="this.delFriend" @startChat="this.startFriendChat"
                        @acceptFriend="this.acceptFriend" @refuseFriend="this.refuseFriend"/>
                    <group-menu v-if="this.menuIndex == 3" :id="this.data.user.id" :groups="this.data.groups"
                        @exitGroup="this.exitGroup" @startChat="this.startGroupChat" @enterGroup="this.enterGroup" />
                </KeepAlive>
            </el-main>
        </el-container>

        <el-dialog title="用户信息" :visible.sync="dialogFormVisible">
            <el-form ref="form" :model="form" :rules="rules" class="reg-container">
                <el-form-item label="头像">
                    <el-upload class="avatar-uploader" action="http://localhost:8088/api/upload" :show-file-list="false"
                        :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" name="file" :data="{id: this.data.user.id}">
                        <img v-if="this.avatar.src" :src="this.avatar.src" class="upload-avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
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
                    <el-button type="primary" style="width: 100%;" @click="dosubmit()">确认修改</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import api from '@/api/api'
import util from '@/api/util'
import ChatMenu from '@/components/menu-chat'
import FriendMenu from '@/components/menu-friend'
import GroupMenu from '@/components/menu-group'
export default {
    name: "user-home",
    components: {
        "chat-menu": ChatMenu,
        "friend-menu": FriendMenu,
        "group-menu": GroupMenu
    },
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
            account: '',
            data: {
                user: {
                    username: '',
                    id: -1
                },
                friends: [],
                groups: [],
                messages: [],
                newfriends: Array,
                chats: Array,
                chatMessage: Map
            }, // from server
            avatar: {
                src: ''
            },
            menuIndex: 0,
            settingDialogVisible: false,
            newmessages: Array,
            dialogFormVisible: false,
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
    mounted() {
        try {
            var account = this.$route.params.account
            this.account = account
            if (account != null) {
                api.user.login(account).then((res) => {
                    this.data = res.data
                    this.avatar.src = util.getUserAvatarSrc(this.data.user.id)
                    // console.log(res.data)
                    this.menuIndex = 1
                    this.newmessages = []

                    var evtSource = new EventSource('http://localhost:8088/sse/event?id=' + this.data.user.id)

                    evtSource.onopen = function () {
                        console.log("sse connection to server open");
                    }

                    evtSource.onmessage = this.processMessage

                    evtSource.onerror = function (e) {
                        console.log("error:" + e)
                    }

                }).catch(function (error) {
                    console.log(error);
                })
            } else {
                this.$router.replace('/')
            }
        } catch {
            this.$router.replace('/')
        }
    },
    methods: {
        switchMenu(i) {
            this.menuIndex = i
            if (i == 1) {
                this.$nextTick(() => {
                    this.newmessages.forEach(message => {
                        this.$refs.chatmenu.processNewMessage(message)
                    });
                    this.newmessages.splice(0, this.newmessages.length)
                })
            }
        },
        delFriend(index) {
            this.data.friends.splice(index, 1)
            this.refresh()
        },
        startFriendChat(friendId) {
            this.menuIndex = 1
            this.$nextTick(() => {
                this.$refs.chatmenu.startFriendChat(friendId)
            })
        },
        refresh() {
            var temp = this.menuIndex
            this.menuIndex = -1
            this.$nextTick(() => {
                this.menuIndex = temp
            })
        },
        processMessage: function (e) {
            if (this.menuIndex == 1) {
                this.$refs.chatmenu.processNewMessage(JSON.parse(e.data))
                this.refresh()
            } else {
                this.newmessages.push(JSON.parse(e.data))
            }
        },
        acceptFriend(friend) {
            this.data.friends.push(friend)
            var index = -1
            for (var i = 0; i < this.data.newfriends.length; i++) {
                if (this.data.newfriends[i].id == friend.id) {
                    index = i
                }
            }
            this.data.newfriends.splice(index, 1)
            this.refresh()
        },
        refuseFriend(friend) {
            var index = -1
            for (var i = 0; i < this.data.newfriends.length; i++) {
                if (this.data.newfriends[i].id == friend.id) {
                    index = i
                }
            }
            this.data.newfriends.splice(index, 1)
            this.refresh()
        },
        exitGroup(index) {
            this.data.groups.splice(index, 1)
            this.refresh()
        },
        enterGroup(group) {
            this.data.groups.push(group)
            // console.log(this.data.groups);
            this.refresh()
        },
        startGroupChat(groupId) {
            this.menuIndex = 1
            this.$nextTick(() => {
                this.$refs.chatmenu.startGroupChat(groupId)
            })
        },
        updateUserInfo() {
            this.dialogFormVisible = true
            this.form.email = this.data.user.email
            this.form.username = this.data.user.username
            this.form.password = this.data.user.password
            this.form.password2 = this.data.user.password
            this.form.sex = this.data.user.sex
            this.form.birth = this.data.user.birth
            this.form.info = this.data.user.info
        },
        dosubmit() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    api.user.updateUser(this.data.user.id,
                        this.form.username,
                        this.form.password,
                        this.form.sex,
                        this.form.birth,
                        this.data.user.email,
                        this.form.info)
                    this.$message({
                        type: 'success',
                        message: '修改个人信息成功!'
                    });
                    this.data.user.email = this.form.email
                    this.data.user.username = this.form.username
                    this.data.user.password = this.form.password
                    this.data.user.sex = this.form.sex
                    this.data.user.birth = this.form.birth
                    this.data.user.info = this.form.info
                    this.dialogFormVisible = false
                }
            })
        },
        handleAvatarSuccess() {
            this.avatar.src = util.getUserAvatarSrc(this.data.user.id) + "?id=" + Math.floor(Math.random()*100)
            this.$message.success("头像上传成功")
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        }
    }
}
</script>

<style lang="scss" scoped>
.home-wrap {
    height: 100vh;

    .el-container {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        width: 60%;
        height: 75%;
        margin: auto;
        background-color: silver;

        .el-aside {
            background-color: #2e2e2e;
            text-align: center;

            .avatar {
                margin: 25px auto 0px;
                width: 36px;
                height: 36px;
            }

            .el-button {
                border: 0;
                background-color: transparent;
                scale: large;

                /deep/ {
                    font-size: 30px;
                }
            }
        }

        .el-main {
            padding: 0;
        }
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .upload-avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
}
</style>