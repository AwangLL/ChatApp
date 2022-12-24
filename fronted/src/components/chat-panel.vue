<template>
    <div class="message-pabel-box">
        <el-button class="eye-more" type="text">加载更多消息</el-button>

        <ul class="message-styles-box">
            <li v-for="(item, index) in messages" :key="index" :class="judgeClass(item.type)">
                <img class="message-avatar" :src="getUserAvatarSrc(item.id)"
                    :alt="item.nickName ? item.nickName : '我是憨批'">

                <p class="message-nickname" v-if="item.type == 'server'">{{ item.nickName }}
                    {{ item.message.time }}</p>
                <p class="message-nickname" v-else>{{ item.message.time }} {{ item.nickName }}</p>
                <p class="message-classic" v-html="item.message.content"></p>
            </li>
        </ul>
    </div>
</template>

<script>
import util from '@/api/util'
export default {
    data() {
        return {
            messages: [
                {
                    id: 0,
                    nickName: 'haha',
                    type: 'server',
                    message: {
                        time: '2022-11-10',
                        content: 'hahaha'
                    }
                },
                {
                    id: 2,
                    nickName: 'baba',
                    type: 'me',
                    message: {
                        time: '2022-12-10',
                        content: 'hi'
                    }
                }
            ]
        }
    },
    computed: {
        getUserAvatarSrc() {
            return function (id) {
                const src = util.getUserAvatarSrc(id)
                return src
            }
        }
    },
    methods: {
        judgeClass(type) {
            if (type === 'server') {
                return 'message-layout-left'
            } else {
                return 'message-layout-right'
            }
        },
    }
}
</script>

<style lang="scss" scoped>
ul,ol {
    list-style: none
}

.message-pabel-box {
  padding: 0 20px;
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  background: rgba(255, 255, 255, .8);

  .eye-more {
    width: 100%;
    padding: 10px 0;
    font-size: 12px;
    text-align: center;
  }

  .message-styles-box {
    margin-bottom: 20px;
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
        background-color: rgba(255, 255, 255, .8);
        &::before {
          left: -16px;
          border-color: transparent rgba(255, 255, 255, .8) transparent transparent;
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
        background-color: rgba(55, 126, 200, .8);
        &::before {
          right: -16px;
          border-color:  transparent transparent  transparent rgba(55, 126, 200, .8);
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
</style>