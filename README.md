#I. Giới thiệu
	PGC là một công cụ được xây dựng trên nền tảng java với mục đích hỗ trợ việc sử lý biểu
	thức,đa thức toán học. Nó cho phép phân tích, tính giá trị của biểu thức, tính đạo hàm...

#II. Các tính năng.
1. Phân tích biểu thức.
2. Tính giá trị biểu thức.
3. Hỗ trợ các hoàm toán học cơ bản.
4. Tối giản đa thức, 
5. Thực hiện các phép toán cộng, trừ, nhân trên đa thức.
5. Tính đạo hàm của một biểu thức theo một biến.
6. Tính Taylor Series của hàm số một biến đến một giá trị bất kì
7. Các tính toán MAXF, MINF, TRUNC, DEDUP theo yêu như mô tả trong file PLDI'14
	   (Hàm MAXF và MINF hỗ trợ hai 2 số).

#III. Sử dụng
##1. Tính giá trị của một biểu thức.
+ Khai báo biến

		Variable x = new Variable("x", 2);
		Variable y = new Variable("y", 3);
		
+ Khai báo bộ phân tích và thêm vào đó các biến

		Parser parser = new Parser();
		parser.add(x);
		parser.add(y);
		
+ Tính giá trị của biểu thức

		String str = "4*x^2*y-3*x^2+5*x*y-1-x";					
		ExpressionProgram eProgram = parser.parse(str);
		double result = eProgram.getVal();
##2. Thực hiện các phép tính trên đa thức
+ Rút gọn đa thức
	
		Variable x = new Variable("x", 2);
		Variable y = new Variable("y", 3);
		String str1 = "3*x*y^4+2*x^(2+3*2)*3*y^3*3+ 2*y^(2+2)*x+2*(4+3)+4+5";
		//Tạo một đa thức
		Polynomial p1 = new Polynomial(str1, var);
> Đa thức sau khi được thu gọn sẽ có dạng:
>> 5.0*x*y^4.0+18.0*x^8.0*y^3.0+23.0
+ Cộng, trừ đa thức
	+ Khai báo 2 đa thức

			Variable[] var = new Variable[3];
			var[0] = new Variable("x");
			var[1] = new Variable("y");
			var[2] = new Variable("z");
			String str = "3*x*y*z-3*x^2+5*x*y-1";
			String str2 = "2*x^2+4*x*y*z-y+2";
			Polynomial p1 = new Polynomial(str, var);
			Polynomial p2 = new Polynomial(str2, var);
	+ Cộng đa thức

			Polynomial sum = MathPolynomial.add(p1, p2);
> Kết quả: 7.0*x*y*z-x^2.0+5.0*x*y+1.0-y
	
	+ Trừ đa thức

			Polynomial sub = MathPolynomial.sub(p1, p2);
> Kết quả: -x*y*z-5.0*x^2.0+5.0*x*y-3.0+y

+ Nhân đa thức

		Variable[] var = new Variable[3];
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		var[2] = new Variable("z");
		String str = "3*x*y-3*x^2+5*x-1";
		String str2 = "2*x^2+4*x*y+2";
		Polynomial p1 = new Polynomial(str, var);
		Polynomial p2 = new Polynomial(str2, var);
		Polynomial multi = MathPolynomial.multi(p1, p2);
> Kết quả: 6.0*x^3.0*y+12.0*x^2.0*y^2.0+6.0*x*y-6.0*x^4.0-
				12.0*x^3.0*y-6.0*x^2.0+10.0*x^3.0+20.0*x^2.0*y+
				10.0*x-2.0*x^2.0-4.0*x*y-2.0

##3. Tính đạo hàm của biểu thức
+ Tính đạo hàm theo biến "x"

		Variable x = new Variable("x", 2);
		Variable y = new Variable("y", 3);
		Parser parser = new Parser();
		parser.add(x);
		parser.add(y);
		String str = "4*x^2*y-3*x^2+5*x*y-1-x";
		ExpressionProgram eProgram = parser.parse(str);
		ExpressionProgram re = eProgram.derivative(x);
> Kết quả : y*4*2*x - 3*2*x + y*5 - 1

##4. Tính Taylor
+ Tính Taylor đến bậc 4 của hàm cos(x) tại điểm có giá trị bằng 0

		String exp = "cosx";
		Variable var = new Variable("x");
		ExpressionProgram ex = TaylorSeries.seies(exp, var, 0, 4);
> Kết quả: 1.0+(-1/2)*x^2+(1/24)*x^4

##5. Thực hiện các hàm MAXF, MINF, TRUNC, DEDUP
+ Khai báo

		Variable[] var = new Variable[3];
		var[0] = new Variable("x");
		var[1] = new Variable("y");
		var[2] = new Variable("z");
		String str = "3*x*y*z-3*x^2+5*x*y-1-x";
		String str2 = "2*x^2+4*x*y*z-y+2";
		Polynomial p1 = new Polynomial(str, var);
		Polynomial p2 = new Polynomial(str2, var);

+ MAXF

		Polynomial maxF = GeneratingFunctions.maxF(p1, p2);
> Kết quả: 4.0*x*y*z+2.0*x^2.0+5.0*x*y+2.0

+ MINF

		Polynomial minF = GeneratingFunctions.minF(p1, p2);
> Kết quả: 3.0*x*y*z-3.0*x^2.0-1.0-x-y
+ TRUNC

		Polynomial trunc = GeneratingFunctions.trunc(p2, var[0], 1);
> Kết quả: 4.0*x*y*z-y+2.0

+ DEDUP

		Polynomial dedup = GeneratingFunctions.dedup(p1);
> Kết quả: x*y*z+x*y






