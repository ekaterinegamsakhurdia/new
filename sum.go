package main


import (
	"fmt"
)


func main() {
// sums upto N
	var n = 0;;
	fmt.Println("Enter integer N ");
    fmt.Scanln(&n)
	var sum = 0;;
	for n > 0 {
		sum=sum+n;
		n = n -1;;
	};
	fmt.Println("The sum of First N natural Numbers is ");
	fmt.Println(sum);
}