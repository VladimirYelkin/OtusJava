можно отдавать всех клиентов через html:

template:
```
<tbody>
  <#list clients as client>
  <tr>
    <td>${client.id}</td>
    <td>${client.name}</td>
    <td>${client.address.streetAddress}</td>
    <td><#list client.phones as phone>${phone.number}</#list></td>
  </tr>
  </#list>
</tbody>
```

java:
```
paramsMap.put("clients",clients);
response.setContentType("text/html;charset=UTF-8");
response.getWriter().println(templateProcessor.getPage(TEMPLATE_ALL_CLIENTS_HTML, paramsMap));
```



