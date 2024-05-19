<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Verify Account</title>
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="center" valign="top" bgcolor="#f7f7f7" style="background-color: #f7f7f7;">
                <br><br>
                <table width="600" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="center" valign="top" bgcolor="#ffffff" style="background-color: #ffffff; font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #000000; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                            <div style="font-size: 24px; color: #333333;">
                                <b>Welcome to Rem Figure Shop!</b>
                            </div>
                            <div style="font-size: 18px; color: #555555; margin-top: 20px;">
                                <p>Dear <b>${name}</b>,</p>
                                <p>Thank you for registering with our application. Please click the link below to verify your email address and complete your registration:</p>
                                <p style="margin-top: 30px;">
                                    <a href="${verifyURL}" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-size: 16px;">VERIFY YOUR EMAIL</a>
                                </p>
                                <p style="margin-top: 40px;">If you did not create an account, no further action is required.</p>
                                <p>Thank you,<br>The Rem Application Team</p>
                            </div>
                        </td>
                    </tr>
                </table>
                <br><br>
            </td>
        </tr>
    </table>
</body>
</html>
