package main


import (
	"fmt"
)


func main() {

//   gcd
    var a = 0;
    fmt.Println("Enter a: ");;
    fmt.Scanln(&a)
    var b = 0
    fmt.Println("Enter b: ");;
    fmt.Scanln(&b)
    for a != b {
        if a > b {
            a = a - b
        }
        if a < b {
            b = b - a
        }
    }
    fmt.Println("The GCD is ")
    fmt.Println(a)
}
