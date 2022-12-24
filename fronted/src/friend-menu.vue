<template>
    <el-container class="chat-wrap">
        <el-aside class="friend-menu">
            <!-- 搜索框 -->
            <el-row style="top: 3px">
                <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="input"
                    style="width: 85%; position: left;" />
                <!-- 添加好友 -->
                <el-button size="large" icon="el-icon-plus" circle @click="this.addFriend" />
            </el-row>
            <!-- 好友列表 -->
            <el-row style="top:10px">
                <el-menu default-active="0" background-color="#e8e6e6" active-text-color="#000000"
                    v-if="this.menuVisible" class="el-menu-vertical" style="border-right: 0;">
                    <!-- 单个好友 -->
                    <el-menu-item v-for="(item, index) in this.friends" :key="index" @click="onclick(index)">
                        <user-tag :user="item" />
                    </el-menu-item>
                </el-menu>
            </el-row>
            <!-- 好友通知 -->
            <!-- <el-row style="position: fixed; top: 77%; text-align: left;">
                <el-badge :is-dot="false" class="item">
                    <el-button type="primary" plain>新朋友</el-button>
                </el-badge>
            </el-row> -->
        </el-aside>
        <el-main style="height:100%">
            <friend-info v-if="this.friend.id != -1" :user="this.friend" :delFriend="this.delFriend"
                :startChat="this.startChat" />
        </el-main>



        <el-dialog title="搜索结果" :visible.sync="dialogTableVisible">

            <el-table :data="this.searchUserResult" v-loading="this.searchUserLoading">
                <el-table-column label="头像" width="80">
                    <template slot-scope="scope">
                        <!-- {{scope.row.id}} -->
                        <el-image style="width: 45px; height: 45px" fit="fill"
                            :src="getUserAvatarSrc(scope.row.id)"></el-image>
                    </template>
                </el-table-column>
                <el-table-column label="用户名" width="200">
                    <template slot-scope="scope">
                        <el-popover trigger="hover" placement="top">
                            <p>ID: {{ scope.row.id }}</p>
                            <p>用户名: {{ scope.row.username }}</p>
                            <p>性别: {{ scope.row.sex }}</p>
                            <p>生日: {{ scope.row.birth }}</p>
                            <p>邮箱: {{ scope.row.email }}</p>
                            <div slot="reference" class="name-wrapper">
                                <el-tag size="medium">{{ scope.row.username }}</el-tag>
                            </div>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column label="个性签名" prop="info" />
                <el-table-column label="添加好友" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button type="success" icon="el-icon-plus" circle size="mini"
                            @click="addThisUser(scope.row)" />
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </el-container>
</template>

<script>
import UserTag from '@/components/user-tag';
import FriendInfo from '@/components/friend-info';
import util from '@/api/util';
import api from '@/api/api';
export default {
    name: 'friend-menu',
    components: {
        "user-tag": UserTag,
        "friend-info": FriendInfo
    },
    props: {
        id: {
            type: Number
        },
        friends: {
            type: Array
        }
    },
    data() {
        return {
            // id: 0,
            // friends: Array,
            index: 0,
            input: '',
            menuVisible: true,
            dialogTableVisible: false,
            searchUserLoading: true,
            searchUserResult: []
        }
    },
    methods: {
        onclick(index) {
            this.index = index
        },
        addFriend() {
            this.$prompt('请输入用户名或ID', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(({ value }) => {
                this.dialogTableVisible = true
                this.searchUserLoading = true
                api.user.searchUser(value).then((res) => {
                    if (res.data != 0) {
                        this.$message({
                            type: 'success',
                            message: '搜索到' + res.data.length + '个符合条件的用户'
                        });
                        this.searchUserResult = res.data
                        this.searchUserLoading = false
                    } else {
                        this.$message({
                            type: 'error',
                            message: "查无此人"
                        });
                        this.dialogTableVisible = false
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '取消输入'
                });
            });
        },
        delFriend() {
            this.$confirm('此操作将永久删除该好友, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.friends.splice(this.index, 1)
                this.index = 0
                this.menuReload()
                util.saveUserFriend(this.id, this.friends)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        startChat() {
        },
        menuReload() {
            this.menuVisible = false
            this.$nextTick(() => {
                this.menuVisible = true
            })
        },
        addThisUser(row) {
            var exist = false
            this.friends.forEach(friend => {
                if (friend.id == row.id) {
                    this.$message({
                        type: 'error',
                        message: row.username + '已经是你的好友，不能重复添加',
                    });
                    exist = true
                }
            });
            if (!exist) {
                api.user.addFriend(this.id, row.id).then((res) => {
                    console.log(res.data)
                    this.$message({
                        type: 'success',
                        message: '已发送好友请求'
                    });
                    this.dialogTableVisible = false
                    this.searchUserResult = []
                }).catch(function (error) {
                    console.log(error)
                    this.$message({
                        type: 'error',
                        message: '发送请求失败'
                    });
                })
            }

        }
    },
    computed: {
        friend: function () {
            if (!this.friends[this.index]) return {
                id: -1,
                username: "1",
            }
            return this.friends[this.index]
        },
        getUserAvatarSrc() {
            return function (id) {
                const src = util.getUserAvatarSrc(id)
                return src
            }
        },
        
    }
}
</script>

<style>
.chat-wrap {
    padding: 0;
    margin: 0 auto;
    /* box-sizing: border-box; */
    background-color: #efefef;
}

.friend-menu {
    height: 668px;
    background-color: #e8e6e6;
    float: left;
}

.item {
    margin-top: 5px;
    margin-right: 0px;
    padding-left: 0;
}
</style>