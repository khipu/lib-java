# khipu

[khipu - Yo pago, yo cobro](https://khipu.com)

Biblioteca JAVA para utilizar los servicios de Khipu.com

Versión Biblioteca: 1.3.3

Versión API Khipu: 1.3
Versión API de notificación: 1.3/1.2

La documentación de Khipu.com se puede ver desde aquí: [https://khipu.com/page/api](https://khipu.com/page/api)

## Licencia

Esta biblioteca se distribuye bajo los términos de la licencia BSD.

## Uso

Si usas Maven en tu proyecto puedes agregar la siguiente dependencia en tu archivo pom.xml

```XML
<dependency>
    <groupId>com.khipu</groupId>
    <artifactId>lib-khipu</artifactId>
    <version>1.3.3</version>
</dependency>
```

Si usas Ivy, Grape, Grails, Buildr, Scala SBT o quieres directamente bajar los .jar puedes buscar khipu en <a href="http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-khipu%22">Maven Central</a>.


## Introducción

Esta biblioteca implementa los siguientes servicios de khipu:

1. Obtener listado de bancos
2. Crear cobros y enviarlos por mail. 
3. Crear una página de Pago.
4. Crear un pago y obtener us URL.
5. Validar notificación de pago usando API de notificaciones 1.3 (recomendado).
6. Validar notificación de pago usando API de notificaciones 1.2 (obsoleto).
7. Verificar el estado de una cuenta de cobro.
8. Verificar el estado de un pago.
9. Marcar un pago como pagado.
10. Marcar un cobro como expirado.
11. Marcar un pago como rechazado.
12. Reversar un pago.


### 1) Obtener listado de bancos.


Este servicio entrega un objeto _KhipuReceiverBanksResponse_ que contiene un listado de los bancos disponibles para efectuar un pago a un cobrador determinado. Cada banco tiene su identificador, un nombre, el monto mínimo que se puede transferir desde él y un mensaje con información importante.

```Java
	KhipuReceiverBanks receiverBanks = Khipu.getReceiverBanks(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	try {
		KhipuReceiverBanksResponse response = receiverBanks.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```

### 2) Crear cobros y enviarlos por mail.
-------------------------------------

Este servicio entrega un objeto _KhipuEmailResponse_ que contiene el identificador
del cobro generado así como una lista de los pagos asociados a este cobro. Por cada 
pago se tiene el ID, el correo asociado y la URL en khipu para pagar.

```Java
	KhipuCreateEmail createEmail = Khipu.getCreateEmail(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	createEmail.setSubject("Un cobro desde java");
	createEmail.setExpiresDate(new Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000)));
	createEmail.addDestinatary("John Doe", "john.doe@gmail.com", 10);
	createEmail.addDestinatary("Jane Dow", "jane.dow@gmail.com", 100);
	createEmail.setSendEmails(true);
	createEmail.setBody("Un cuerpo para este cobro");
	try {
		KhipuEmailResponse response = createEmail.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```

### 3) Crear una página de Pago.
----------------------------

Este ejemplo genera un archivo .html con un formulario de pago en khipu. La fecha de expiración está puesta para 3 días más.

```Java
	PrintWriter out;
	try {
		out = new PrintWriter("form.html");
		out.print(Khipu.getPaymentButton(ID_DEL_COBRADOR
			, SECRET_DEL_COBRADOR
			, "john.doe@gmail.com"
			, "Evdfk"
			, "Prueba de cobro"
			, "descripción de la prueba"
			, 10
			, new Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000))
			, "https://ejemplo.com/notify"
			, "https://ejemplo.com/return"
			, "https://ejemplo.com/cancel"
			, "https://ejemplo.com/picture"
			, "datos personalizados"
			, "EEE87"
			, Khipu.BUTTON_100x50));
		out.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
```


### 4) Crear un pago y obtener su URL.

Este servicio entrega un objeto _KhipuUrlResponse_ que contiene el identificador de un pago generado, su URL en khipu y la URL para iniciar el pago desde un dispositivo móvil.

```Java
    KhipuCreatePaymentURL createPaymentUrl = Khipu.getCreatePaymentURL(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
    createPaymentUrl.setSubject("Un cobro desde java");
    createPaymentUrl.setBody("Un cuerpo para este cobro");
    createPaymentUrl.setEmail("john.doe@gmail.com");
    createPaymentUrl.setAmount(1000);

    try {
        KhipuUrlResponse response = createPaymentUrl.execute();
        System.out.println(response);
    } catch (KhipuException e) {
        System.out.println(e.getType());
        System.out.println(e.getMessage());
    } catch (IOException e) {
        e.printStackTrace();
    }
```

### 5) Validar la notificación de un pago usando API de notificaciones 1.3 o superior (recomendado).

Este servicio se usa para obtener la información de un pago luego de que khipu notifique que este ha sido pagado. Khipu envía la notificación de pago
que contiene un _notification_token_. Este token se envía de vuelta a khipu para obtener la información del pago. En especial, se debe verificar que el campo
_receiver_id_ sea el mismo configurado en el sitio.

El servicio entrega un objeto _KhipuGetPaymentNotificationResponse_ que contiene la información del pago.

```Java
KhipuGetPaymentNotification khipuGetPaymentNotification = Khipu.gePaymentNotification(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
khipuGetPaymentNotification.setNotificationToken("c67879f947fc61d36fa1419764bcb7a96c5593e5fb1833f31f5141acff050cdd");
try {
	KhipuGetPaymentNotificationResponse response = khipuGetPaymentNotification.execute();
	System.out.println(response);
} catch (KhipuException e) {
	System.out.println(e.getType());
	System.out.println(e.getMessage());
} catch (IOException e) {
	e.printStackTrace();
}
```

### 6) Validar la notificación de un pago usando API de notificaciones 1.2 o anterior (obsoleto).

Este ejemplo contacta a khipu para validar los datos de una transacción. Para usar
este servicio no es necesario configurar el SECRET del cobrador. Se retorna un
KhipuVerifyPaymentNotificationResponse cuyo método isVerified devuelve la validez de la
información.

*Importante:* En este ejemplo los parámetros están configurados a mano, pero en producción los
datos deben obtenerse desde el HttpRequest.


```Java
	KhipuVerifyPaymentNotification verifyPaymentNotification = Khipu.getVerifyPaymentNotification(ID_DEL_COBRADOR);
	verifyPaymentNotification.setApiVersion("1.2");
	verifyPaymentNotification.setNotificationId("aq1td2jl2uen");
	verifyPaymentNotification.setSubject("Motivo de prueba");
	verifyPaymentNotification.setAmount("12575");
	verifyPaymentNotification.setCurrency("CLP");
	verifyPaymentNotification.setTransaction_id("FTEEE5SWWO");
	verifyPaymentNotification.setPayerEmail("john.doe@gmail.com");
	verifyPaymentNotification.setCustom("Custom info");
	verifyPaymentNotification.setNotificationSignature("j8kPBHaPNy3PkCh...hhLvQbenpGjA==");
	try {
		KhipuVerifyPaymentNotificationResponse response = verifyPaymentNotification.execute();
		System.out.println(response.isVerified());
	} catch (KhipuException e) {
  		System.out.println(e.getType());
  		System.out.println(e.getMessage());
  	} catch (IOException e) {
  		e.printStackTrace();
  	}
```


### 7) Verificar el estado de una cuenta de cobro.

Este servicio permite consultar el estado de una cuenta khipu. Se devuelve un 
KhipuReceiverStatusResponse que indica si esta cuenta está habilitada para cobrar
y el tipo de cuenta (desarrollo o producción).

```Java
	KhipuReceiverStatus receiverStatus = Khipu.getReceiverStatus(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	try {
		KhipuReceiverStatusResponse response = receiverStatus.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```

### 8) Verificar el estado de un pago.

Este servició sirve para verificar el estado de un pago.

```Java
	KhipuPaymentStatus paymentStatus = Khipu.getPaymentStatus(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	paymentStatus.setPaymentId("9fnsggqqi8ho");
	try {
		KhipuPaymentStatusResponse response = paymentStatus.execute();
		System.out.println(response);
	}  catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```

### 9) Marcar un cobro como pagado.

Este servicio permite marcar un cobro como pagado. Si el pagador
paga por un método alternativo a khipu, el cobrador puede marcar 
este cobro como saldado.

```Java
	KhipuSetPayedByReceiver setPayedByReceiver = Khipu.getSetPayedByReceiver(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	setPayedByReceiver.setPaymentId("54dhfsch6avd");
	try {
		KhipuSetPayedByReceiverResponse response = setPayedByReceiver.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```

### 10) Marcar un cobro como expirado.

Este servicio permite adelantar la expiración del cobro. Se puede adjuntar un texto
que será desplegado a la gente que trate de ir a pagar. 

```Java
	KhipuSetBillExpired setBillExpired = Khipu.getSetBillExpired(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	setBillExpired.setBillId("udmEe");
	setBillExpired.setText("Plazo vencido");
	try {
		KhipuSetBillExpiredResponse response = setBillExpired.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```

### 11) Marcar un cobro como rechazado.

Este servicio permite rechazar un pago con el fin de inhabilitarlo. Permite indicar la razón por la que el pagador rechaza saldar este pago:

```Java
	KhipuSetRejectedByPayer setRejectedByPayer = Khipu.getSetRejectedByPayer(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	setRejectedByPayer.setPaymentId("0pk7xfgocry4");
	setRejectedByPayer.setText("El pago no corresponde");
	try {
		KhipuSetRejectedByPayerResponse response = setRejectedByPayer.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```


### 12) Reversar un pago.

Este servicio permite reversar un pago para que el dinero sea devuelto al pagador. Para más detalles, como por ejemplo hasta cuando es posible reversar, lee
la documentación en la [página de la API](https://khipu.com/page/api):

```Java
	KhipuInstantReverse instantReverse = Khipu.getInstantReverse(ID_DEL_COBRADOR, SECRET_DEL_COBRADOR);
	instantReverse.setNotificationId("0pk7xfgocry4");
	try {
		KhipuInstantReverseResponse response = instantReverse.execute();
		System.out.println(response);
	} catch (KhipuException e) {
		System.out.println(e.getType());
		System.out.println(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
	}
```
