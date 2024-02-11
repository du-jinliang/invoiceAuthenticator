package cn.wenhe.invoice.checkFp.tencent;

import cn.wenhe.invoice.checkFp.CheckFpStrategyAbstract;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceVerifyNewRequest;
import com.tencentcloudapi.ocr.v20181119.models.VatInvoiceVerifyNewResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 腾讯票据查验策略实现类
 * @url: https://cloud.tencent.com/document/product/866/73674
 * @date: 2024/2/7
 */
@Component("tencentCheckFpStrategyImpl")
public class TencentCheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "tencent";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        try{
            Credential cred = new Credential(
                    TencentSecretProperties.KEY,
                    TencentSecretProperties.SECRET
            );
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            OcrClient client = new OcrClient(cred, "ap-beijing", clientProfile);
            VatInvoiceVerifyNewRequest req = new VatInvoiceVerifyNewRequest();

            String invoiceType = requestDto.getInvoiceType();
            if(Objects.equals("21", invoiceType)) {
                invoiceType = "08";
            }
            if(Objects.equals("22", invoiceType)) {
                invoiceType = "10";
            }
            if(Objects.equals("16", invoiceType)) {
                invoiceType = "32";
            }

            String checkCode = requestDto.getCheckCode();
            if (checkCode.length() > 6) {
                checkCode = checkCode.replace(" ", "");
                checkCode = checkCode.substring(checkCode.length() - 6);
            }

            req.setInvoiceCode(requestDto.getInvoiceCode());
            req.setInvoiceNo(requestDto.getInvoiceNo());
            req.setInvoiceDate(requestDto.getInvoiceDate());
            req.setInvoiceKind(invoiceType);
            req.setCheckCode(checkCode);
            req.setAmount(requestDto.getInvoiceAmount());
            req.setAmount(requestDto.getInvoiceAmount());

            VatInvoiceVerifyNewResponse resp = client.VatInvoiceVerifyNew(req);

            return new CheckFpResponseDto("验真成功", true);
        } catch (TencentCloudSDKException e) {
            return new CheckFpResponseDto("验真失败", false);
        }
    }
}
