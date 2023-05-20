<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>List集合数据遍历</title>
</head>
<body>

<#-- list 数据的展示 -->
<b>展示list中的stu数据:</b>
<br>
<br>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>

    <#--list集合遍历-->
    <#list stuList as stu>
    <#--隔行换色-->
        <#if stu_index%2 ==0>
            <tr style="color:#ff0000">
                <#--遍历行号-->
                <td>${stu_index+1}</td>
                <#--遍历姓名-->
                <td>${stu.name}</td>
                <#--遍历年龄-->
                <td>${stu.age}</td>
                <#--遍历金额-->
                <td>${stu.money}</td>
            </tr>
        <#else >
            <tr style="color: blueviolet">
                <#--遍历行号-->
                <td>${stu_index+1}</td>
                <#--遍历姓名-->
                <td>${stu.name}</td>
                <#--遍历年龄-->
                <td>${stu.age}</td>
                <#--遍历金额-->
                <td>${stu.money}</td>
            </tr>
        </#if>

    </#list>

    list集合的大小：${stuList?size}
</table>
</body>
</html>