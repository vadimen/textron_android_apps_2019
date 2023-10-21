using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication1.Models
{
    public class Produs
    {

        public int id
        {
            get;set;
        }

        public string nume
        {
            get; set;
        }

        public string categorie { get; set; }

        public decimal pret
        {
            get; set;
        }
    }
}