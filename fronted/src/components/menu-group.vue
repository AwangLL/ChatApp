<template>
    <div class="group-menu-wrap">
        <el-container>
            <el-aside width="300px">
                <el-header height="50px">
                    <el-input placeholder="请输入群名" prefix-icon="el-icon-search" v-model="input" />
                    <el-button icon="el-icon-plus" @click="this.searchGroup" />
                </el-header>
                <ul class="friend-list" v-if="input == ''">
                    <li v-for="(item, idx) in groups" :key="idx" @click="selectGroup(idx)"
                        :class="['friend-item', { 'friend-active': idx == index }]">
                        <div class="friend-item-left">
                            <img class="friend-avatar" :src="getGroupAvatarSrc(item.id)" />
                        </div>
                        <div class="friend-item-right">
                            <div class="friend-id">{{ item.name }}</div>
                            <!-- <div class="friend-name">{{ item.info }}</div> -->
                        </div>
                    </li>
                </ul>
                <ul class="friend-list" v-else>
                    <li v-for="(item, idx) in this.searchGroupList" :key="idx" @click="selectGroup(idx)"
                        :class="['friend-item', { 'friend-active': idx == index }]">
                        <div class="friend-item-left">
                            <img class="friend-avatar" :src="getGroupAvatarSrc(item.id)" />
                        </div>
                        <div class="friend-item-right">
                            <div class="friend-id">{{ item.name }}</div>
                            <!-- <div class="friend-name">{{ item.info }}</div> -->
                        </div>
                    </li>
                </ul>
            </el-aside>
            <el-main class="friend-info" v-if="index != -1">
                <el-row>
                    <el-col :span="5" style="text-align: right;">
                        <el-image style="width: 80px; height: 80px" :src="getGroupAvatarSrc(getActiveGroup.id)"
                            fit="fill" />
                    </el-col>
                    <el-col :span="10" :push="5" style="text-align:left; ">
                        <!-- username -->
                        <el-row style="font-size: 40px;">
                            {{ getActiveGroup.id }}
                        </el-row>
                        <!-- id -->
                        <el-row style="margin-top: 10px; font-size: 20px;">
                            {{ getActiveGroup.name }}
                        </el-row>
                    </el-col>
                    <el-row style="margin-top: 30%;">
                        <el-col :span="10" style="text-align: left;">
                            <el-button type="danger" icon="el-icon-delete" @click="exitGroup()">退出群聊</el-button>
                        </el-col>

                        <el-col :span="10" style="text-align: right;">
                            <el-button type="success" icon="el-icon-position" @click="startChat()">发起聊天</el-button>
                        </el-col>
                    </el-row>
                </el-row>

            </el-main>
        </el-container>
    </div>
</template>

<script>
import api from '@/api/api';
import util from '@/api/util';
export default {
    props: {
        "id": {
            type: Number,
            required: true
        },
        "groups": {
            type: Array,
            required: true
        }
    },
    data() {
        return {
            input: "",
            index: -1
        }
    },
    methods: {
        searchGroup() {
            this.$prompt('请输入要加入的群聊ID', '提示', {
                confirmButtonText: '加入',
                cancelButtonText: '取消',
            }).then(({ value }) => {
                api.user.searchGroup(value).then((res) => {
                    if (res.data.name != undefined) {
                        var found = false
                        this.groups.forEach((group) => {
                            // console.log(group.id + ",,," + res.data.id);
                            if (group.id == res.data.id) {
                                found = true
                                this.$message({
                                    type: 'error',
                                    message: "已在该群聊"
                                });
                            }
                        })
                        if (found == false) {
                            api.user.enterGroup(res.data.id, this.id)
                            this.$message({
                                type: 'success',
                                message: '加入群聊' + res.data.name
                            });
                            this.$emit("enterGroup", res.data)
                        }

                    } else {
                        this.$message({
                            type: 'error',
                            message: "没有符合条件的群聊"
                        });
                    }
                }).catch(() => {
                    this.$message({
                        type: 'error',
                        message: '没有符合条件的群聊'
                    })
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '取消输入'
                });
            });
        },
        selectGroup(idx) {
            this.index = idx
        },
        exitGroup() {
            this.$confirm('此操作将永久退出群聊, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                api.user.exitGroup(this.groups[this.index].id, this.id)
                this.$emit("exitGroup", this.index)
                this.index = -1
            }).catch((e) => {
                console.log(e);
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        startChat() {
            this.$emit("startChat", this.groups[this.index].id)
        }
    },
    computed: {
        getGroupAvatarSrc() {
            return function (id) {
                const src = util.getGroupAvatarSrc(id)
                return src
            }
        },
        searchGroupList() {
            var temp = []
            this.groups.forEach(group => {
                if (group.name.indexOf(this.input) > -1) {
                    temp.push(group)
                }
            });
            return temp
        },
        getActiveGroup() {
            return this.groups[this.index]
        },
    }
}
</script>

<style lang="scss" scoped>
.group-menu-wrap {
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

        .friend-info {
            height: 100%;
            padding-top: 20%;
            padding-left: 10%;
            padding-right: 10%;
        }
    }
}
</style>