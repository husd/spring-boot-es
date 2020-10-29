package com.example.demo.redis;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 抽奖项目模拟100W条记录占用多大内存
 *
 * @author hushengdong
 */
public class NumTest {

    @Test
    public void test() throws IOException {

        int max = 10;
        StringBuilder sb = new StringBuilder(50 * max);
        long a = 100019466301L;
        for (int i = 0; i < max; i++) {
            sb.append("hset 5N:PRELIMINARY_DRAW_20201111_0 ");
            sb.append(a + i);
            //sb.append(" abc");
            sb.append(" \"[{'win_id':1000000,'prizeInx':0,'ticketCode':'72345678','memberId':'100019466301',}]\" ");
            sb.append("\n");
        }
        writeNIO(sb.toString(), "husd.lst");
    }

    @Test
    public void test2() throws IOException {

        int max = 1000000;
        //int max = 10;
        StringBuilder sb = new StringBuilder(50 * max);
        long a = 82603568L;
        for (int i = 0; i < max; i++) {
            sb.append("set e5:AC_TICKET_20201111_");
            sb.append(a + i);
            sb.append(" \"334732_2020-10-28 16:08:12\"");
            sb.append("\n");
        }
        long b = 10;
        long c = a++ + b;
        writeNIO(sb.toString(), "husd2.lst");
    }

    /**
     * 把字符串写入到文件内容中去
     *
     * @param content
     * @param name
     * @throws IOException
     */
    public static void writeNIO(String content, String name) throws IOException {
        FileOutputStream fos = new FileOutputStream(name, true);
        FileChannel channel = fos.getChannel();
        ByteBuffer buf = ByteBuffer.wrap(content.getBytes());
        buf.put(content.getBytes());
        buf.flip();
        channel.write(buf);
        channel.close();
        fos.close();
    }
}
