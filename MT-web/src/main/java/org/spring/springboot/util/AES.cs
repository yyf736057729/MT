using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Xml;
using Oracle.DataAccess.Client;

namespace ConsoleApplication1
{
    class AES
    {
        static void Main(string[] args)
        {
            string number = AesEncrypt("13444444444", "123");
            string token = AesEncrypt("token", "123");
            string sign = AesEncrypt("token&13444444444", "4523567890194556");
            Console.ReadKey();
        }

        /// <summary>
        ///  AES 加密
        /// </summary>
        /// <param name="str">明文（待加密）</param>
        /// <param name="key">密文</param>
        /// <returns></returns>
        public static string AesEncrypt(string str, string skey)
        {
            if (string.IsNullOrEmpty(str)) return null;
            Byte[] toEncryptArray = Encoding.UTF8.GetBytes(str);

            RijndaelManaged rm = new RijndaelManaged
            {
                Key = Encoding.UTF8.GetBytes(skey),
                Mode = CipherMode.ECB,
                Padding = PaddingMode.PKCS7
            };

            ICryptoTransform cTransform = rm.CreateEncryptor();
            Byte[] buf = cTransform.TransformFinalBlock(toEncryptArray, 0, toEncryptArray.Length);

            String hex = BitConverter.ToString(buf, 0).Replace("-", string.Empty).ToLower();
            return hex;
        }


               
        /// <summary>
        ///  AES 解密
        /// </summary>
        /// <param name="str">明文（待解密）</param>
        /// <param name="key">密文</param>
        /// <returns></returns>
        public static string AesDecrypt(string str, string skey)
        {
            if (string.IsNullOrEmpty(str)) return null;
            Byte[] toEncryptArray = new Byte[str.Length / 2];

            int beg = 0;
            for (int i = 0; i < str.Length / 2; i++)
            {
                Byte c = Convert.ToByte(str.Substring(beg, 2), 16);
                toEncryptArray[i] = c;
                beg += 2;
            }

            RijndaelManaged rm = new RijndaelManaged
            {
                Key = Encoding.UTF8.GetBytes(skey),
                Mode = CipherMode.ECB,
                Padding = PaddingMode.PKCS7
            };

            ICryptoTransform cTransform = rm.CreateDecryptor();
            Byte[] resultArray = cTransform.TransformFinalBlock(toEncryptArray, 0, toEncryptArray.Length);

            return Encoding.UTF8.GetString(resultArray);
        }
    }
}
