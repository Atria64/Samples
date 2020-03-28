using System.IO;
using System.Threading.Tasks;
using BlazorInputFile;
using Microsoft.AspNetCore.Hosting;

namespace ConvertGray
{
    public interface IBlazorFileUp
    {
        Task<string> UploadAsync(IFileListEntry files);
    }
    public class FileUpService:IBlazorFileUp
    {
        private readonly IWebHostEnvironment _env;

        public FileUpService(IWebHostEnvironment env)
        {
            _env = env;
        }

        public async Task<string> UploadAsync(IFileListEntry files)
        {
            var path = Path.Combine(_env.WebRootPath, "Upload", files.Name);
            using(MemoryStream ms = new MemoryStream())
            {
                await files.Data.CopyToAsync(ms);
                using(FileStream fs = new FileStream(path, FileMode.Create, FileAccess.Write))
                {
                    ms.WriteTo(fs);
                }
            }
            return path;
        }
    }
}
