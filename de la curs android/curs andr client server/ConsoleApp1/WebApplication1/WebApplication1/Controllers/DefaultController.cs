using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebApplication1.Models;

namespace WebApplication1.Controllers
{
    public class DefaultController : ApiController
    {
        private static List<Produs> listaProduse = new List<Produs>();

        static DefaultController()
        {
            var newInstance = new DefaultController();

            newInstance.AdaugareProdus(
                new Produs
                {
                    id = 1,
                    nume = "TurboRoaba",
                    categorie = "Unelte",
                    pret = 1000
                }
                );

            newInstance.AdaugareProdus(
                new Produs
                {
                    id = 2,
                    nume = "TurboRoaba zzzzzz",
                    categorie = "Unelte",
                    pret = 2000
                }
                );


            newInstance.AdaugareProdus(
                new Produs
                {
                    id = 3,
                    nume = "TurboRoaba xxxxx",
                    categorie = "Unelte",
                    pret = 200
                }
                );

            newInstance.AdaugareProdus(
                new Produs
                {
                    id = 3,
                    nume = "TurboRoaba ccccc",
                    categorie = "Unelte",
                    pret = 600
                }
                );

           
        }
        public IEnumerable<Produs> GetProduse()
            {
                return listaProduse;
            }

        public IHttpActionResult GetProduse(int id)
        {
            var product = listaProduse.FirstOrDefault((p) => p.id == id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        private bool AdaugareProdus(Produs prod)
        {
            if (prod.id != 0 && prod.pret != 0 && prod.nume != null && prod.categorie != null)
            {
                Produs prodTemp = listaProduse.FirstOrDefault(p => p.id == prod.id);
                if (prodTemp != null)
                {
                    prodTemp.nume = prod.nume;
                    prodTemp.categorie = prod.categorie;
                    prodTemp.pret = prod.pret;
                    return true;
                }
                listaProduse.Add(prod);
                
            }
            return true;
        }
    }


}
