package cn.wenhe.invoice.checkFp;

import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import cn.wenhe.invoice.config.http.HttpsClientRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 票据查验策略抽象类
 * @date: 2024/2/7
 */
public abstract class CheckFpStrategyAbstract implements CheckFpStrategy{

    protected RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());

    @Override
    public CheckFpResponseDto checkFp(CheckFpRequestDto requestDto) {
        updateRealInvoiceType(requestDto);

        return doCheck(requestDto);
    }

    protected void updateRealInvoiceType(CheckFpRequestDto requestDto) {
        String invoiceNo = requestDto.getInvoiceNo();
        String invoiceCode = requestDto.getInvoiceCode();
        String checkCode = requestDto.getCheckCode();
        String invoiceTypeInput = requestDto.getInvoiceType();
        String invoiceType = "";

        if(Objects.equals("全电票普票", invoiceTypeInput)) {
            requestDto.setInvoiceType("22");
            return;
        }

        if (StringUtils.hasText(invoiceCode) && invoiceCode.length() == 10) {
            char subChar = invoiceCode.charAt(7);
            switch (subChar) {
                case '1':
                case '5':
                    invoiceType = "01"; // 增值税专用发票
                    break;
                case '2':
                case '7':
                    invoiceType = "02"; // 货物运输业增值税专用发票
                    break;
                case '3':
                case '6':
                    invoiceType = "04"; // 增值税普通发票
                    break;
            }
        } else if (StringUtils.hasText(invoiceCode) &&  invoiceCode.length() == 12) {
            if (Arrays.asList("144031539110", "131001570151", "133011501118", "111001571071").contains(invoiceCode)) {
                invoiceType = "10"; // 增值税普通发票(电子)
            } else if (invoiceCode.startsWith("0")) {
                String subStr = invoiceCode.substring(10);
                switch (subStr) {
                    case "11":
                        invoiceType = "10"; // 增值税普通发票(电子)
                        break;
                    case "04":
                    case "05":
                        invoiceType = "04"; // 增值税普通发票
                        break;
                    case "06":
                    case "07":
                        invoiceType = "11"; // 增值税普通发票(卷式)
                        break;
                    case "12":
                        invoiceType = "14"; // 通行费发票
                        break;
                    case "17":
                        invoiceType = "15"; // 二手车发票
                        break;
                    case "13":
                        invoiceType = "08"; // 电子专用发票
                        break;
                }
            } else {
                char subChar = invoiceCode.charAt(7);
                if (subChar == '2') {
                    invoiceType = "03"; // 机动车销售统一发票
                }
            }
        } else if (invoiceNo.length() == 20) {
            if (StringUtils.hasText(checkCode) && checkCode.equals("22")) {
                invoiceType = "22";
            } else {
                invoiceType = "21"; // 全电发票专用发票
            }
        }



        requestDto.setInvoiceType(invoiceType);
    }

    protected abstract CheckFpResponseDto doCheck(CheckFpRequestDto requestDto);
}
