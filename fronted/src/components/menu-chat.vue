<template>
    <div class="chat-menu-wrap">
        <el-container>
            <el-aside width="250px">
                <ul class="chat-list">
                    <li v-for="(item, idx) in chats" :key="idx" @click="selectChat(idx)"
                        :class="['chat-item', { 'chat-active': idx == index }]">
                        <div class="chat-item-left">
                            <el-badge class="item" :max="99" :value="getNewMessageCnt(item.key)"
                                :hidden="getNewMessageCnt(item.key) == 0">
                                <!-- 好友 -->
                                <img v-if="item.type == 0" class="chat-avatar" :src="getUserAvatarSrc(item.id)" />
                                <!-- 群组 -->
                                <img v-else class="chat-avatar" :src="getGroupAvatarSrc(item.id)" />
                            </el-badge>
                        </div>
                        <div class="chat-item-right">
                            <div class="chat-header">
                                <div v-if="item.type == 0" class="chat-title">
                                    {{ getFriendName(item.id) }}</div>
                                <div v-else class="chat-title">{{ getGroupName(item.id) }}</div>
                                <div class="chat-time" v-if="getLastMessage(item.key) != null">
                                    {{ getLastMessage(item.key).time.substr(11) }}</div>
                            </div>
                            <div class="chat-content" v-if="getLastMessage(item.key) != null">
                                {{ getLastMessage(item.key).content }}</div>
                        </div>
                    </li>
                </ul>
            </el-aside>
            <!-- 聊天区 -->
            <el-main v-if="this.index != -1">
                <!-- 标题 -->
                <el-header height="40px">
                    <span v-if="this.chats[index].type == 0" class="title">
                        {{ getFriendName(this.chats[index].id) }}</span>
                    <span v-else class="title">{{ getGroupName(this.chats[index].id) }}</span>
                </el-header>
                <!-- 消息区 -->
                <div class="message-pabel-box">
                    <!-- 加载历史消息 -->
                    <el-button class="eye-more" type="text" @click="loadHistoryMessage()">加载更多消息</el-button>
                    <!-- 消息记录 -->
                    <ul class="message-styles-box">
                        <!-- 群消息 -->
                        <li v-for="(item, idx) in this.messages.get(this.chats[this.index].key).messages" :key="idx"
                            :class="judgeClass(item.fromId)">
                            <img class="message-avatar" :src="getUserAvatarSrc(item.fromId)">

                            <p class="message-nickname" v-if="item.gotoId == id"> {{ item.fromId }} {{ item.time }} </p>
                            <p class="message-nickname" v-else> {{ item.time }} {{ item.fromId }} </p>
                            <p class="message-classic" v-html="item.content"></p>
                        </li>
                    </ul>
                </div>
                <!-- 输入区 -->
                <div class="message-input-box">
                    <div class="input-tools">
                        <i slot="reference" class="el-icon-s-opportunity" title="表情"></i>
                    </div>

                    <el-input type="textarea" resize="none" :autosize="{ minRows: 3, maxRows: 3 }" v-model="textArea">
                    </el-input>

                    <div class="footer-tools">
                        <el-button size="mini" type="primary" @click="sendMessage()" class="send-button">
                            发送/Send
                        </el-button>
                    </div>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import api from '@/api/api';
import util from '@/api/util';
export default {
    props: {
        ready_message: {
            type: Array,
            required: true
        },
        friends: {
            type: Array,
            required: true
        },
        groups: {
            type: Array,
            required: true
        },
        id: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            index: -1,
            textArea: "",
            chats: Array,
            messages: Map
        }
    },
    methods: {
        refresh() {
            var temp = this.index
            this.index = -1
            this.$nextTick(() => {
                this.index = temp
            })
        },
        judgeClass(fromId) {
            if (fromId == this.id || fromId == undefined) return 'message-layout-right'
            else return 'message-layout-left'
        },
        selectChat(idx) {
            this.index = idx
            this.messages.get(this.chats[this.index].key).cnt = 0
            this.messages.get(this.chats[this.index].key).messages.forEach((message) => {
                if (message.fromId != this.id) {
                    message.read = true
                    api.user.readMessage(message.id, this.id)
                }
            })
        },
        processNewMessage(message) {
            if (message.type == 0) {
                let key = "u-" + message.fromId
                if (this.messages.has(key)) { // 已经存在
                    if(this.chats[this.index].key == key) {
                        message.read = true
                        api.user.readMessage(message.id, this.id)
                    }
                    this.messages.get(key).messages.push(message)
                    if (message.fromId != this.id) this.messages.get(key).messages.cnt += 1
                } else {
                    let temp = 0
                    if (message.fromId != this.id) temp = 1
                    this.messages.set(key, { cnt: temp, messages: [message] })
                    this.chats.push({ key: key, type: 0, id: message.fromId })
                }
            } else {
                let key = "g-" + message.gotoId
                if (this.messages.has(key)) { // 已经存在
                    if(this.chats[this.index].key == key) {
                        message.read = true
                        api.user.readMessage(message.id, this.id)
                    }
                    this.messages.get(key).messages.push(message)
                    if (message.fromId != this.id) this.messages.get(key).messages.cnt += 1
                } else {
                    let temp = 0
                    if (message.fromId != this.id) temp = 1
                    this.messages.set(key, { cnt: temp, messages: [message] })
                    this.chats.push({ key: key, type: 1, id: message.gotoId })
                }
            }
            this.refresh()
        },
        startFriendChat(friendId) {
            var key = 'u-' + friendId
            if(this.messages.get(key)) {
                for(var i = 0; i < this.chats.length; i++){
                    if(this.chats[i].key == key) {
                        this.index = i
                    }
                }
            } else {
                this.chats.push({ key: key, id: friendId, type: 0 })
                this.messages.set(key, { cnt: 0, messages: [] })
                this.index = this.chats.length - 1
            }
        },
        startGroupChat(groupId) {
            var key = 'g-' + groupId
            if(this.messages.get(key)) {
                for(var i = 0; i < this.chats.length; i++){
                    if(this.chats[i].key == key) {
                        this.index = i
                    }
                }
            } else {
                this.chats.push({ key: key, id: groupId, type: 1 })
                this.messages.set(key, { cnt: 0, messages: [] })
                this.index = this.chats.length - 1
            }
        },
        sendMessage() {
            var fromId = this.id
            var gotoId
            if (this.chats[this.index].type == 0) {
                gotoId = this.chats[this.index].id
            } else {
                gotoId = this.chats[this.index].id
            }
            var type = this.chats[this.index].type

            var message = {
                from: fromId,
                goto: gotoId,
                type: type,
                time: util.dateFormat("YYYY-mm-dd HH:MM:SS", new Date()),
                content: this.textArea
            }

            api.user.sendMessage(message)

            message = {
                fromId: fromId,
                gotoId: gotoId,
                type: type,
                time: util.dateFormat("YYYY-mm-dd HH:MM:SS", new Date()),
                content: this.textArea
            }

            this.textArea = ""
            this.messages.get(this.chats[this.index].key).messages.push(message)
        },
        loadHistoryMessage() {
            var type = this.chats[this.index].type
            if (type == 0) {
                let id
                if (this.getFirstMessage(this.chats[this.index].key).id != null) {
                    id = this.getFirstMessage(this.chats[this.index].key).id
                } else id = 1 << 32
                api.user.getFriendHistoryMessage(this.id, this.chats[this.index].id,
                    id).then((res) => {
                        var temp = this.messages.get(this.chats[this.index].key).messages
                        this.messages.get(this.chats[this.index].key).messages = res.data
                        var ref = this.messages.get(this.chats[this.index].key).messages

                        temp.forEach((message) => {
                            ref.push(message)
                        })

                        this.refresh()
                    })
            } else {
                let id
                if (this.getFirstMessage(this.chats[this.index].key).id != null) {
                    id = this.getFirstMessage(this.chats[this.index].key).id
                } else id = 1 << 32

                api.user.getGroupHistoryMessage(this.chats[this.index].id,
                    id).then((res) => {
                        var temp = this.messages.get(this.chats[this.index].key).messages
                        this.messages.get(this.chats[this.index].key).messages = res.data
                        var ref = this.messages.get(this.chats[this.index].key).messages

                        temp.forEach((message) => {
                            ref.push(message)
                        })

                        this.refresh()
                    })
            }
        }
    },
    mounted() {
        this.chats = new Array
        this.messages = new Map
        this.ready_message.forEach(message => {
            if (message.type == 0) {
                let key = "u-" + message.fromId
                if (this.messages.has(key)) { // 已经存在
                    this.messages.get(key).messages.push(message)
                    if (message.fromId != this.id) this.messages.get(key).messages.cnt += 1
                } else {
                    let temp = 0
                    if (message.fromId != this.id) temp = 1
                    this.messages.set(key, { cnt: temp, messages: [message] })
                    this.chats.push({ key: key, type: 0, id: message.fromId })
                }
            } else {
                let key = "g-" + message.gotoId
                if (this.messages.has(key)) { // 已经存在
                    this.messages.get(key).messages.push(message)
                    if (message.fromId != this.id) this.messages.get(key).messages.cnt += 1
                } else {
                    let temp = 0
                    if (message.fromId != this.id) temp = 1
                    this.messages.set(key, { cnt: temp, messages: [message] })
                    this.chats.push({ key: key, type: 1, id: message.gotoId })
                }
            }
        });
        this.index = -1
    },
    computed: {
        getUserAvatarSrc() {
            return function (id) {
                const src = util.getUserAvatarSrc(id)
                return src
            }
        },
        getGroupAvatarSrc() {
            return function (id) {
                const src = util.getGroupAvatarSrc(id)
                return src
            }
        },
        getNewMessageCnt() {
            return function (key) {
                return this.messages.get(key).cnt
            }
        },
        getFirstMessage() {
            return function (key) {
                let messages = this.messages.get(key).messages
                if (messages.length == 0) return null
                return messages[0]
            }
        },
        getLastMessage() {
            return function (key) {
                let messages = this.messages.get(key).messages
                if (messages.length == 0) return null
                return messages[messages.length - 1]
            }
        },
        getFriendName() {
            return function (id) {
                for (var i = 0; i < this.friends.length; i++) {
                    if (this.friends[i].id == id) {
                        return this.friends[i].username
                    }
                }
            }
        },
        getGroupName() {
            return function (id) {
                for (var i = 0; i < this.groups.length; i++) {
                    if (this.groups[i].id == id) {
                        return this.groups[i].name
                    }
                }
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.chat-menu-wrap {
    background-color: #efefef;
    height: 100%;
    width: 100%;

    .el-container {
        height: 100%;

        .el-aside {
            background-color: #eae8e6;

            .chat-active {
                background: #c6c4c4;
            }

            .chat-list {
                overflow: auto;

                .chat-item {
                    display: flex;
                    padding: 10px 15px;
                    width: 100%;
                    height: 62px;
                    font-size: 12px;
                    box-sizing: border-box;

                    &:hover {
                        background: #c6c4c4;
                    }

                    .chat-item-left {
                        margin-right: 10px;
                        font-size: 0;

                        .chat-avatar {
                            width: 40px;
                            height: 40px;
                        }
                    }

                    .chat-item-right {
                        flex: 1;
                        overflow: hidden;

                        .chat-header {
                            display: flex;
                            justify-content: space-between;

                            .chat-title {
                                width: 100%;
                                font-size: 14px;
                            }

                            .chat-time {
                                color: #1a1a1a;
                            }
                        }

                        .chat-content {
                            margin-top: 4px;
                            color: #000000;
                            overflow: hidden;
                            white-space: nowrap;
                            text-overflow: ellipsis;
                        }
                    }

                }
            }

        }

        .el-main {
            height: 100%;
            padding: 0;
            background-color: #f5f5f5;

            .el-header {
                position: relative;
                line-height: 40px;
                background: rgb(55, 126, 200);
                overflow: hidden;

                .icon-message {
                    color: #ffffff;
                    font-size: 20px;
                    vertical-align: middle;
                    font-size: 20px;
                    vertical-align: middle;
                }

                .title {
                    color: #ffffff;
                    font-size: 20px;
                    vertical-align: middle;
                    display: inline-block;
                    margin-left: 5px;
                    font-size: 16px;
                    letter-spacing: 1px;
                }
            }

            .message-pabel-box {
                padding: 0 20px;
                flex: 1;
                overflow-y: auto;
                -webkit-overflow-scrolling: touch;
                background: rgba(255, 255, 255, .8);
                height: 72%;

                .eye-more {
                    width: 100%;
                    padding: 10px 0;
                    font-size: 12px;
                    text-align: center;
                }

                .message-styles-box {
                    margin-bottom: 20px;
                    height: 500px;

                    .message-layout-left,
                    .message-layout-right {
                        margin-top: 20px;
                        width: 100%;

                        .message-classic::before {
                            content: '';
                            position: absolute;
                            border-width: 8px;
                            border-style: solid;
                        }
                    }

                    .message-layout-left {
                        .message-avatar {
                            float: left;
                            margin-right: 10px;
                        }

                        .message-classic {
                            background-color: rgba(55, 126, 200, 0.8);

                            &::before {
                                left: -16px;
                                border-color: transparent rgba(55, 126, 200, 0.8) transparent transparent;
                            }
                        }
                    }

                    .message-layout-right {
                        text-align: right;

                        .message-avatar {
                            float: right;
                            margin-left: 10px;
                        }

                        .message-classic {
                            text-align: left;
                            color: #ffffff;
                            background-color: rgba(149, 236, 105, .8);

                            &::before {
                                right: -16px;
                                border-color: transparent transparent transparent rgba(149, 236, 105, .8);
                            }
                        }
                    }

                    .message-avatar {
                        width: 40px;
                        height: 40px;
                        border-radius: 2px;
                        border: 1px solid #eeeeee;
                    }

                    .message-nickname {
                        color: #777777;
                        font-size: 12px;
                    }

                    .message-classic {
                        position: relative;
                        max-width: 45%;
                        margin-top: 5px;
                        display: inline-block;
                        padding: 9px 12px;
                        font-size: 14px;
                        color: #333333;
                        border-radius: 5px;
                        white-space: pre-line;
                        word-break: break-all;
                    }
                }
            }

            .message-input-box {
                height: 150px;
                background-color: rgba(255, 255, 255, .85);
                border-top: 1px solid #dddddd;

                .input-tools {
                    position: relative;
                    padding-left: 10px;
                    padding-top: 10px;

                    .upload-demo {
                        display: inline;
                    }

                    i {
                        margin-left: 10px;
                        color: rgb(94, 94, 94);
                        font-size: 20px;
                        cursor: pointer;
                    }
                }

                .el-textarea {
                    .el-textarea__inner {
                        padding: 5px 20px;
                        border-radius: 0;
                        border: 0;
                        background-color: transparent;
                    }
                }

                .footer-tools {
                    text-align: right;

                    .send-button {
                        padding: 7px 10px;
                        margin-right: 20px;
                        background: #377ec8;
                    }
                }
            }

            .face-pabel {
                .face {
                    display: inline-block;
                    width: 20px;
                    height: 20px;
                }
            }
        }
    }
}
</style>