<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>

<#-- Map 数据的展示 -->
<h1>map集合取值</h1>
<br/><br/>
<a href="###">方式一：通过map['keyname'].property</a><br/>
输出stu1的学生信息：<br/>
姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>
<br/>


<a href="###">方式二：通过map.keyname.property</a><br/>
输出stu2的学生信息：<br/>
姓名：${stuMap.stu2.name}<br/>
年龄：${stuMap.stu2.age}<br/>
<br/>

<h1>map集合遍历</h1>
<a href="###">方式一：遍历map中两个学生信息：</a><br/>
<table>


    <#assign key=stuMap?keys>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list key as k>
    <tr>
        <td>${k_index}</td>
        <td>${stuMap[k].name}</td>
        <td>${stuMap[k].age}</td>
        <td>${stuMap[k].money}</td>
    </tr>
    </#list>

</table>


<hr>
<a href="###">方式二：遍历map中两个学生信息：</a><br/>
<table>
    <#assign value = stuMap?values>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list value as v>
    <tr>
        <td>${v_index}</td>
        <td>${v.name}</td>
        <td>${v.age}</td>
        <td>${v.money}</td>
    </tr>
    </#list>
</table>
<hr>

</body>
</html>