package khrystal.me.pipe;

import me.khrystal.util.ipc.IPCCallback;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public interface IMessagePIPE {

    void clientSend(Message message, IPCCallback callback);
}
