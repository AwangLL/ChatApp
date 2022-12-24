<template>
    <div class="friend-menu-wrap">
        <el-container>
            <el-aside width="300px">
                <el-header height="50px">
                    <el-input placeholder="请输入用户名" prefix-icon="el-icon-search" v-model="input" />
                    <el-button icon="el-icon-plus" @click="this.searchFriend" />
                </el-header>
                <!-- 搜索栏为空时 -->
                <ul class="friend-list" v-if="input == ''">
                    <li :class="['friend-item', { 'friend-active': -1 == index }]" @click="index = -1"
                        style="margin-bottom: 30px;">
                        <div class="friend-item-left">
                            <img class="friend-avatar" src="http://localhost:8088/api/files/new_friend.png" />
                        </div>
                        <div class="friend-item-right"
                            style="font-size: large; vertical-align: middle; line-height: 42px;">
                            新的朋友
                        </div>
                    </li>
                    <li v-for="(item, idx) in friends" :key="idx" @click="selectFriend(idx)"
                        :class="['friend-item', { 'friend-active': idx == index }]">
                        <div class="friend-item-left">
                            <img class="friend-avatar" :src="getUserAvatarSrc(item.id)" />
                        </div>
                        <div class="friend-item-right">
                            <div class="friend-id">{{ item.username }}</div>
                            <div class="friend-name">{{ item.info }}</div>
                        </div>
                    </li>
                </ul>
                <!-- 搜索栏不为空时 -->
                <ul class="friend-list" v-else>
                    <li v-for="(item, idx) in this.searchFriendList" :key="idx" @click="selectFriend(locateFriend(idx))"
                        :class="['friend-item', { 'friend-active': locateFriend(idx) == index }]">
                        <div class="friend-item-left">
                            <img class="friend-avatar" :src="getUserAvatarSrc(item.id)" />
                        </div>
                        <div class="friend-item-right">
                            <div class="friend-id">{{ item.username }}</div>
                            <div class="friend-name">{{ item.info }}</div>
                        </div>
                    </li>
                </ul>
            </el-aside>
            <el-main v-if="this.index == -1" class="new-friend">
                <el-table :data="this.newfriends" :show-header="false">
                    <el-table-column label="头像" width="80">
                        <template slot-scope="scope">
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
                    <el-table-column label="拒绝/接受" width="100" align="center">
                        <template slot-scope="scope">
                            <el-button type="success" icon="el-icon-close" circle size="mini"
                                @click="refuseFriend(scope.row)" />
                            <el-button type="success" icon="el-icon-check" circle size="mini"
                                @click="acceptFriend(scope.row)" />
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-main v-else class="friend-info">
                <el-row>
                    <el-col :span="5" style="text-align: right;">
                        <el-image style="width: 80px; height: 80px" :src="getUserAvatarSrc(getActiveFriend.id)"
                            fit="fill" />
                    </el-col>
                    <el-col :span="10" :push="5" style="text-align:left; ">
                        <!-- username -->
                        <el-row style="font-size: 40px;">
                            {{ getActiveFriend.username }}
                        </el-row>
                        <!-- id -->
                        <el-row style="margin-top: 10px; font-size: 20px;">
                            {{ getActiveFriend.id }}
                        </el-row>
                    </el-col>
                </el-row>
                <el-descriptions style="margin: 10px" :column="2">
                    <el-descriptions-item label="性别">
                        {{ getActiveFriend.sex }}</el-descriptions-item>
                    <el-descriptions-item label="出生日期">
                        {{ getActiveFriend.birth }}</el-descriptions-item>
                    <el-descriptions-item label="邮箱" :span="2">
                        {{ getActiveFriend.email }}</el-descriptions-item>
                    <el-descriptions-item label="个性签名" :span="2">
                        {{ getActiveFriend.info }}</el-descriptions-item>
                </el-descriptions>
                <el-row style="margin-top: 30%;">
                    <el-col :span="10" style="text-align: left;">
                        <el-button type="danger" icon="el-icon-delete" @click="delFriend()">删除好友</el-button>
                    </el-col>

                    <el-col :span="10" style="text-align: right;">
                        <el-button type="success" icon="el-icon-position" @click="startChat()">发起聊天</el-button>
                    </el-col>
                </el-row>
            </el-main>

            <!-- 搜索好友 -->
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
                                @click="addFriend(scope.row)" />
                        </template>
                    </el-table-column>
                </el-table>
            </el-dialog>
        </el-container>
    </div>

</template>
    
<script>
import util from '@/api/util'
import api from '@/api/api'
export default {
    props: {
        "id": {
            type: Number,
            required: true
        },
        "friends": {
            type: Array,
            required: true
        },
        "newfriends": {
            type: Array,
            required: true
        },
    },
    data() {
        return {
            input: '', // 搜索框内容
            index: -1,
            dialogTableVisible: false,
            searchUserLoading: false,
            searchUserResult: []
        }
    },
    methods: {
        selectFriend(idx) {
            this.index = idx
        },
        searchFriend() {
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
                }).catch(() => {
                    this.$message({
                        type: 'error',
                        message: '用户名或ID信息错误'
                    })
                    this.dialogTableVisible = false
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '取消输入'
                });
            });
        },
        addFriend(row) {
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
            if (this.id == row.id) {
                this.$message({
                    type: 'error',
                    message: '不能添加自己为好友'
                });
                exist = true
            }
            if (!exist) {
                api.user.addFriend(this.id, row.id).then(() => {
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
                api.user.delFriend(this.id, this.friends[this.index].id)
                this.$emit("delFriend", this.index)
                this.index = -1
            }).catch((e) => {
                console.log(e);
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        acceptFriend(friend) {
            api.user.acceptFriend(this.id, friend.id)
            this.$emit("acceptFriend", friend)
        },
        refuseFriend(friend) {
            api.user.refuseFriend(this.id, friend.id)
            this.$emit("refuseFriend", friend)
        },
        startChat() {
            this.$emit("startChat", this.friends[this.index].id)
        },
        locateFriend(idx) {
            var id = this.searchFriendList[idx].id
            for (var i = 0; i < this.friends.length; i++) {
                if (this.friends[i].id == id) {
                    return i
                }
            }
            return -1
        }
    },
    mounted() {
    },
    computed: {
        getUserAvatarSrc() {
            return function (id) {
                const src = util.getUserAvatarSrc(id)
                return src
            }
        },
        getActiveFriend() {
            return this.friends[this.index]
        },
        searchFriendList() {
            var temp = []
            this.friends.forEach(friend => {
                if (friend.username.indexOf(this.input) > -1) {
                    temp.push(friend)
                }
            });
            return temp
        }
    }
}
</script>

<style lang="scss" scoped>
.friend-menu-wrap {
    background-color: #efefef;
    height: 100%;
    width: 100%;

    .el-container {
        height: 100%;

        .el-aside {
            background-color: #eae8e6;

            .el-header {
                padding: 5px;

                .el-input {
                    width: 234px;
                }
            }

            .friend-active {
                background: #c6c4c4;
            }

            .friend-list {
                overflow: auto;

                .friend-item {
                    display: flex;
                    padding: 10px 15px;
                    width: 100%;
                    height: 62px;
                    font-size: 12px;
                    box-sizing: border-box;

                    &:hover {
                        background: #c6c4c4;
                    }

                    .friend-item-left {
                        margin-right: 10px;
                        font-size: 0;

                        .friend-avatar {
                            width: 40px;
                            height: 40px;
                        }
                    }

                    .friend-item-right {
                        flex: 1;
                        overflow: hidden;

                        .friend-id {
                            display: flex;
                            justify-content: space-between;
                            width: 100%;
                            font-size: medium;
                            color: #000000;
                        }

                        .friend-name {
                            margin-top: 1px;
                            color: #000000;
                            font-size: medium;
                            overflow: hidden;
                            white-space: nowrap;
                            text-overflow: ellipsis;
                        }
                    }
                }
            }
        }

        .new-friend {
            height: 100%;
            padding: 20px;

            .el-table {
                background-color: black;
            }
        }

        .friend-info {
            height: 100%;
            padding: 15%;
        }
    }
}
</style>