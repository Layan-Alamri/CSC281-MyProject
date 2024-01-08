package CS281_Project;

public class CS281Project {
	    
	       public static int[] LCG(int seed, int quantity) {
		    	 System.out.println("Hi there! This is LCG method, I am called with");
		         System.out.println("(seed=" + seed + "  quantity=" + quantity + ")");
		         System.out.println("and I have these initialized local variables:");
		         
		    			
		        // Parameters for the linear congruential generator
		        int a = 1664525;
		        int c = 1013904223;
		        int m = 32767 ;
		        System.out.println("(A = " + a + "  C = " + c + "  M = " + m + ")");

		        // Initialize the array to store the random numbers
		        int[] randomNumbers = new int[quantity];

		        // Initialize the seed
		        int currentSeed = seed;

		        // Generate the random numbers
		        for (int i = 0; i < quantity; i++) {
		            currentSeed = (a * currentSeed + c) % m;
		            
		         // Ensure the generated number is positive
		            if (currentSeed < 0) {
		                currentSeed += m;
		            }
		            
		            randomNumbers[i] = currentSeed;
		        }
		        System.out.println("I generated " + quantity + " random numbers, and I made them all positives!");

			     // Print the random numbers
			        for (int i = 0; i < randomNumbers.length; i++) {
			            System.out.print(randomNumbers[i]+",");
			        }
		        System.out.println();
		        System.out.println("bye now! --LGC method");
		        

		        return randomNumbers;
		    }
	//---------------------------------------------------------------------------------------
	    // This function is called for all k trials.
	    // It returns false if n is composite and returns false if n is probably prime. 
	    // d is an odd number such that d*2^r = n-1
	    // for some r >= 1
	     public static boolean millerTest(long d, long n) {
	        // Pick a random number a such that 1<a<n-1
	        // base cases make sure that n%2 = 0
	        long a = 2 + (long)(Math.random() % (n - (n%2)));

	        // Compute a^d % n
	        long x = modularExponentiation(a, d, n);

	        // If a^d ≡ 1 (mod n) or a^d ≡ -1 (mod n), then n passes this iteration of the test
	        if (x == 1 || x == n - 1)
	            return true; // prime

	        // Keep squaring x while one of the following doesn't happen
	        // (i) d does not reach n-1
	        // (ii) (x^2) % n is not 1
	        // (iii) (x^2) % n is not n-1
	        while (d != n - 1) {
	            x = (x * x) % n;
	            d *= 2;

	             // If a^(2^i * d) ≡ 1 (mod n), then n is composite number
	            if (x == 1) return false;
	            // If a^(2^i * d) ≡ -1 (mod n), then n is probably prime number
	            if (x == n - 1) return true;
	        }

	        // Return composite
	        return false;
	    }

	    // It returns false if n is composite and returns true if n is probably prime.
	    // k is an input parameter that determines accuracy level.
	    // Higher value of k indicates more accuracy.
	    public static boolean millerRabinTest(long n, int k) {
	        // base cases
	        if(n <= 1){
	            return false ;}//negative number or 0 cannot be considered probably prime number

	        if (n == 2 || n == 3){
	            return true;}  //this mean n = 2 or n = 3, which is a probably prime number

	        if (n %2 == 0){
	            return false;}
	        
	        long r = 0;
	        long d = n - 1;
	        
	        // Find r and d such that n-1 = 2^r * d
	        while (d % 2 == 0) {
	            d /= 2;
	            r++;
	        }

	        // Iterate given number of 'k' times
	        for (int i = 0; i < k; i++)
	            if (!millerTest(d, n))
	                return false;

	        return true;
	    }
	    //---------------------------------------------------------------------------------------------
	   public static KeyPair generateKeys(){ //altered 
	       
	        System.out.println("Calling LCG..");
	        int[] t= LCG(5,100);
	        
	        System.out.println("back to generateKeys, now I will examin the random numbers and assign" + " p to the first number that passes millerRabinTest");
	        System.out.println("q to the second number (if it is not equal to p... duh!)");
	        int p = 0;
		    int q = 0;
	        int i;
	        for(i=0;i<t.length;i++) {
	        	if(millerRabinTest(t[i],4)) {
	        		System.out.println("p is " +t[i]+ "     this is the "+ (i+1)+"th element in the random list");
	        		p=t[i];
	        		break;
	        	}}
	
	        for(int x=i+1;x<t.length;x++) {
	        	if(millerRabinTest(t[x],4)) {
	        		System.out.println("p is " +t[x]+ "     this is the "+ (x+1)+"th element in the random list");
	        		q=t[x];
	        		break;
	        	}}
	        int n = p*q;
	        long phi = (p-1)*(q-1);
	        System.out.println("I calculated phi: " + phi);
	        long e = 65537; 
	        System.out.println("I set e: " + e);
	        long d = extendedEuclideanAlgorithm(e, phi);
	        System.out.println("I called extendedEuclideanAlgorithm, and got d to be: " + d);
	        System.out.println("finally, I am creating an instance of KeyPair class as:");
	        System.out.println("KeyPair(new PublicKey(n, e), new PrivateKey(n, d))");
	        System.out.println("and returning it. Bye now! --generateKeys method");

	       return new KeyPair(new PublicKey(n,e), new PrivateKey(n,d)); 
	    }
	//--------------------------------------------------------------------------------------

	static int gcd(int e, int z)
	    {
	        if (e == 0)
	            return z;
	        else
	            return gcd(z % e, e);

	}
	//-------------------------------------------------------------------------------------
	    public static long extendedEuclideanAlgorithm(long e, long m){
	        long m0 = m;
	        long n0 = 0, n1 = 1;

	        if (m == 1) {
	            return 0;
	        }

	        // Apply extended Euclidean algorithm
	        while (e > 1) {
	            long q = e/ m;
	            long t = m;

	            m = e% m;
	            e= t;

	            t = n0;
	            n0 = n1 - q * n0;
	            n1 = t;
	        }

	        // Make x1 positive
	        if (n1 < 0) {
	            n1 += m0;
	        }

	        // Check if a and b are coprime
	        if (e!= 1) {
	            return -1;
	    }
	        
	 return n1;
	    }
	    
	    //-------------------------------------------------------------------------------------
	    
	public static long[] encrypt(String message, long e, long n) {
			
			System.out.println("Hi there! This is encrypt method");
			System.out.println("converting my string to int:");
			
			int[] iArr = String_to_intArray(message);
			
			for(int i=0 ; i < iArr.length ;i++)
			    System.out.print(iArr[i]+"   ");
			    System.out.println();
			
			long[] enc = new long[iArr.length];
			
			for(int j=0 ; j < iArr.length ; j++)
				enc[j] = modularExponentiation(iArr[j],e,n);
			
			System.out.println("encryptedValues:");
			for(int k=0 ; k < enc.length ; k++)
				System.out.print(enc[k]+" ");
			
			System.out.println();
				
			System.out.println("bye now! --encrypt method");
							
				return enc ;
			}
	      
	      	public static String decrypt(long[] ciphertext, long d, long n) {
			
			System.out.println("Hi there! This is decrypt method");
			int[] dec = new int[ciphertext.length];
			
			for(int i=0 ; i < ciphertext.length ;i++)
				dec[i] = (int) modularExponentiation(ciphertext[i], d, n);
			
			System.out.println("decryptedValues:");
			
			for(int j=0 ; j < dec.length ;j++)
				System.out.print(dec[j]+"   ");
			
			 System.out.println();
			
			 String decInt_to_decStr = IntArray_to_String(dec) ;
			
			 System.out.println("ArrayToString:"+decInt_to_decStr);
			
			 System.out.println("bye now! --decrypt method");
			
			return decInt_to_decStr ;
		        }

	        public static int[] String_to_intArray(String str) {
			
			char[] c = str.toCharArray();
			
			for(int i=0 ; i < c.length ;i++)
				c[i] = (char)(c[i] - 96) ;
			
			int[] a = new int[c.length] ;
			
			
			
			for(int j=0 ; j < c.length ;j++)
				a[j] = (int)c[j] ;
		
			
		return a ;
		}
		
		//convert array of long to string example [8,5,12,12,15] should return “hello”
		public static String IntArray_to_String (int[] array) {
			
			char[] c = new char[array.length];
			
			for(int i=0 ; i < c.length ;i++)
				c[i] = (char) array[i]; //convert int to char
			
			for(int k=0 ; k < c.length ;k++)
				c[k] = (char) (c[k]+96) ;
			
			
			String str = "" ;
			for(int j=0 ; j < c.length ;j++)
				str += c[j];
			
			
			return str ;
		}
		
	        //Efficiently computes the result of raising a base to an exponent modulo a certain number, as we saw in Chapter4
		public static long modularExponentiation(long base, long exponent, long modulus) {
			
			long result = 1 ;
	      
			while(exponent > 0) {
				
				if(exponent % 2 == 1)
				result = (result*base)%modulus ;
				
				base = (base*base) % modulus ;
				exponent /= 2 ;
			
			}
			
			result = result % modulus ;
			
			return result;
		}
	//-------------------------------------------------------------------------------------------
	    public static void main(String args[]) {
	        System.out.println("Hi there! This is the main method");
	        
	        System.out.println("calling generateKeys");
	        KeyPair kp= generateKeys();

	        System.out.println("setting plaintext to: hello");
	        String plaintext= "hello";
		System.out.println("calling encrypt...");
		long[] enc = encrypt(plaintext,kp.getPublicKey().getE(),kp.getPublicKey().getN());
				
				
		System.out.println("calling decrypt on encrypt output...");
		String decryptedText= decrypt(enc,kp.getPrivateKey().getD(),kp.getPrivateKey().getN());
		
				
		System.out.println("making sure decryptedText and plaintext are equalsIgnoreCase...");
				
		if(decryptedText.equalsIgnoreCase("hello"))
	            System.out.println("Yes! they are");
		else
	            System.out.println("No!");
				
				
		System.out.println("bye now! --main method");
		System.out.println();	
	    }
	}//End class

