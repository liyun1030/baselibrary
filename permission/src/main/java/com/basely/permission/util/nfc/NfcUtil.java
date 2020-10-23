package com.basely.permission.util.nfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * TODO:功能说明 Nfc工具类
 */
public class NfcUtil {
    private static final String TAG = "NfcUtil";
    private static volatile NfcUtil instance;
    private Context context;

    private NfcUtil(Context ctx) {
        this.context = ctx;
    }

    public static NfcUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (NfcUtil.class) {
                if (instance == null) {
                    instance = new NfcUtil(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 检查NFC是否打开,-1不支持nfc,1打开，０关闭
     */
    public int  getNfcState() {
        try {
            NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
            if (mNfcAdapter == null) {
//            Toast.makeText(activity, "设备不支持NFC功能!", Toast.LENGTH_SHORT).show();
                return -1;
            } else {
                if (!mNfcAdapter.isEnabled()) {
//                IsToSet();
                    return 0;
                } else {
//                Toast.makeText(activity, "NFC功能已打开!", Toast.LENGTH_SHORT).show();
                    return 1;
                }
            }
        }catch (Exception e){
            return -1;
        }
    }
    public void openNfc() {
        enable();
    }

    public boolean isSupportNfc() {
        try {
            PackageManager packageManager = context.getPackageManager();
            boolean b1 = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
            return b1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 关闭
     */
    public void closeNfc() {
//        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
//        mNfcAdapter.((Activity) context);
        disable();
    }

    /**
     * open NFC
     */
    private void enable() {
        try {
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
            Method method = adapter.getClass().getDeclaredMethod("enable");
            method.invoke(adapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * close NFC
     */
    private void disable() {
        try {
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
            Method method = adapter.getClass().getDeclaredMethod("disable");
            method.invoke(adapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取NFC的数据
     */
    public String readNFCFromTag(Intent intent) throws UnsupportedEncodingException {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                return readResult;
            }
        }
        return "";
    }


    /**
     * 往nfc写入数据
     */
    public void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        NdefRecord ndefRecord = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ndefRecord = NdefRecord.createTextRecord(null, data);
        }
        NdefRecord[] records = {ndefRecord};
        NdefMessage ndefMessage = new NdefMessage(records);
        ndef.writeNdefMessage(ndefMessage);
    }

    /**
     * 读取nfcID
     */
    private String readNFCId(Intent intent) throws UnsupportedEncodingException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String id = ByteArrayToHexString(tag.getId());
        return id;
    }

    /**
     * 将字节数组转换为字符串
     */
    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private void IsToSet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否跳转到设置页面打开NFC功能");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToSet();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void goToSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
            return;
        }
    }

    /**
     * 解析 ndefRecord 文本数据
     *
     * @param ndefRecord
     * @return
     */
    public static String parse(NdefRecord ndefRecord) {
        // verify tnf   得到TNF的值
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }

        // 得到字节数组进行判断
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }

        try {
            // 获得一个字节流
            byte[] payload = ndefRecord.getPayload();
            // payload[0]取第一个字节。 0x80：十六进制（最高位是1剩下全是0）
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8"
                    : "UTF-16";
            // 获得语言编码长度
            int languageCodeLength = payload[0] & 0x3f;
            // 获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength,
                    "US-ASCII");
            //
            String text = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);

            return text;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }


    //初次判断是什么类型的NFC卡
    public static NdefMessage[] getNdefMsg(Intent intent) {
        if (intent == null)
            return null;

        //nfc卡支持的格式
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return null;
        }
        String[] temp = tag.getTechList();
        for (String s : temp) {
            Log.i(TAG, "resolveIntent tag: " + s);
        }


        String action = intent.getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Parcelable[] rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] ndefMessages;

            // 判断是哪种类型的数据 默认为NDEF格式
            if (rawMessage != null) {
                Log.i(TAG, "getNdefMsg: ndef格式 ");
                ndefMessages = new NdefMessage[rawMessage.length];
                for (int i = 0; i < rawMessage.length; i++) {
                    ndefMessages[i] = (NdefMessage) rawMessage[i];
                }
            } else {
                //未知类型
                Log.i(TAG, "getNdefMsg: 未知类型");
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable parcelable = intent
                        .getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(parcelable).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
                        empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                ndefMessages = new NdefMessage[]{msg};
            }


            return ndefMessages;
        }

        return null;
    }


    //一般公家卡，扫描的信息
    private static String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        return sb.toString();
    }


    private static String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private static long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private static long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

}
