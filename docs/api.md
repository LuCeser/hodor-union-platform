# 1. 权限管理

## 1.1 提交权限

`POST {baseUrl}/v1.0/authentication`

**请求Body参数**

| 参数            | 属性   | 必须 | 描述                         | 举例 |
| --------------- | ------ | ---- | ---------------------------- | ---- |
| engine          | string | 是   | 引擎类型                     | ALI  |
| appAccessKey    | string | 是   | 开放平台提供的app access key |      |
| appAccessSecret | string | 是   | 开放平台提供的app access     |      |
| appId           | string | 否   | 开放平台提供的appId          |      |

**请求示例**

```

```

**响应码**

201

## 1.2 更新权限

`PUT {baseUrl}/v1.0/authentication?engine={engineType}&field=appAccessKey,appId`

**请求参数**

| 参数   | 属性   | 必须 | 描述                                    | 举例 |
| ------ | ------ | ---- | --------------------------------------- | ---- |
| engine | string | 是   | 需要修改的引擎类型                      | ALI  |
| field  | string | 是   | 需要修改的字段，多个字段使用','进行分隔 |      |
|        |        |      |                                         |      |

**请求Body**

| 参数            | 属性   | 必须 | 描述                         | 举例 |
| --------------- | ------ | ---- | ---------------------------- | ---- |
| engine          | string | 是   | 引擎类型                     | ALI  |
| appAccessKey    | string | 是   | 开放平台提供的app access key |      |
| appAccessSecret | string | 是   | 开放平台提供的app access     |      |
| appId           | string | 否   | 开放平台提供的appId          |      |

**请求示例**

**响应码**

204

## 1.3 查询权限信息

`GET {baseUrl}/v1.0/authentication?engine={engineType}`

**请求参数**

| 参数   | 属性   | 必须 | 描述                                   | 举例 |
| ------ | ------ | ---- | -------------------------------------- | ---- |
| engine | string | 否   | 需要查询的开发平台，如果为空则查询所有 | ALI  |

**请求示例**

```

```

**响应码**

200

## 1.4 删除权限信息

`DELETE {baseUrl}/v1.0/authentication?engine={engineType}`

**请求参数**

| 参数   | 属性   | 必须 | 描述                                   | 举例 |
| ------ | ------ | ---- | -------------------------------------- | ---- |
| engine | string | 否   | 需要删除的开发平台，如果为空则删除所有 | ALI  |

**请求示例**

```

```

**响应码**

204

# 2. 语音转文本功能

# 3. 热词管理