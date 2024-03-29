#  各产品发票验真准确率测试项目

## 项目初衷

调研并测试各个产品在增值税发票验真方面的准确度和验真速度

## 已调研产品

| 序号 | 厂商名   | 免费次数 | 付费选项（元/1000次） | 正确率 | 单条验真速度 | 备注                        |
| ---- | -------- | -------- | --------------------- | ------ | ------------ | --------------------------- |
| 1    | 航天金税 |          |                       | 60%    | 1327ms       |                             |
| 2    | 百度     | 20       | 240                   | 100%   | 1886ms       |                             |
| 3    | 腾讯     | 50       | 280                   | 100%   | 2187ms       |                             |
| 4    | 中安未来 | 3        | 169                   | 100%   | 2188ms       |                             |
| 5    | 票查查   | 0        | 150左右（按单次计费） |        |              | 单次请求时间过长            |
| 6    | 深智恒际 | 10       | 128                   | 100%   | 700ms        | 第二次请求时速度提升成221ms |
| 7    | 聚美智数 | 60       | 140                   | 36%    |              |                             |
| 8    | 广州税行 |          |                       |        |              | 接口超时                    |
| 9    | 用友     |          |                       |        |              | 接口过期                    |
| 10   | TextIn   |          |                       |        |              | 昂贵                        |
| 11   | 八戒财税 |          |                       |        |              | 接口超时                    |
| 12   | 百旺     |          |                       |        |              | 没有发票验真                |
| 13   | 来也     |          |                       |        |              | 整个平台                    |
| 14   | 睿真票据 |          |                       |        |              | 注册没过                    |
| 15   | 费耘     |          |                       |        |              | 整个平台                    |
| 16   | APISpace |          |                       |        |              | 测试没一个过的              |
| 17   | 百望云   |          |                       |        |              | 需面向客户单独开通          |
| 18   | 聚合数据 |          |                       |        |              | 仅限企业用户                |

## 测试项目使用

1. 在`application.yml`配置密钥信息

2. 启动项目

3. 使用如`postman`接口测试工具，按如下参数提交请求

   ![image-20240211171231735](https://tuchuang.wenhe9.cn/img/image-20240211171231735.png)

4. `type`接口参数如下：

   - | 产品名称 | 类型名称 |
     | -------- | -------- |
     | 聚美智数 | anna     |
     | 百度     | baidu    |
     | 航天金税 | js       |
     | 票查查   | piaocc   |
     | 广州税行 | rh       |
     | 深智恒际 | sz       |
     | 腾讯     | tencent  |
     | 中安未来 | za       |

5. `excel`内容格式如下：

   - ![image-20240211171946003](https://tuchuang.wenhe9.cn/img/image-20240211171946003.png)

6. 测试项目日志输出如下：

   - ![image-20240211172120935](https://tuchuang.wenhe9.cn/img/image-20240211172120935.png)

7. 同时在项目根目录会生成一个`票据测试结果.xlsx`的excel文件，内容如下：

   - ![image-20240211172350781](https://tuchuang.wenhe9.cn/img/image-20240211172350781.png)