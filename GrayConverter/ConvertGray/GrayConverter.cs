using System;
using Microsoft.AspNetCore.Hosting;
using System.Drawing;
using System.IO;


namespace ConvertGray
{
    public interface IGrayConverter
    {
        public bool GrayConvert(string path);
    }
    public class GrayConverter : IGrayConverter
    {
        private readonly IWebHostEnvironment _env;

        public GrayConverter(IWebHostEnvironment env)
        {
            _env = env;
        }

        public bool GrayConvert(string path)
        {
            try
            {
                Image original = new Bitmap(path);

                Bitmap newImage = new Bitmap(original.Width, original.Height);

                using (Graphics g = Graphics.FromImage(newImage))
                {
                    //グレースケール処理
                    System.Drawing.Imaging.ColorMatrix cm =
                            new System.Drawing.Imaging.ColorMatrix(
                            new float[][]{
                            new float[]{0.299f, 0.299f, 0.299f, 0 ,0},
                            new float[]{0.587f, 0.587f, 0.587f, 0, 0},
                            new float[]{0.114f, 0.114f, 0.114f, 0, 0},
                            new float[]{0, 0, 0, 1, 0},
                            new float[]{0, 0, 0, 0, 1}
                            });

                    System.Drawing.Imaging.ImageAttributes ia =
                        new System.Drawing.Imaging.ImageAttributes();
                    ia.SetColorMatrix(cm);

                    g.DrawImage(original,
                        new Rectangle(0, 0, original.Width, original.Height),
                        0, 0, original.Width, original.Height, GraphicsUnit.Pixel, ia);

                }

                //newImageにはグレースケール化された画像が格納されている
                var newpath = Path.Combine(_env.WebRootPath, "Upload", "Gray"+Path.GetFileName(path));
                newImage.Save(newpath);
                return true;
            }
            catch (Exception)
            {
                //失敗時
                return false;
            }
        }
    }
}
