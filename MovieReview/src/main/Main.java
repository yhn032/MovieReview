package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import dao.LoginDao;
import dao.MovieDao;
import dao.ReviewDao;
import vo.CategoryVo;
import vo.LoginVo;
import vo.MovieVo;
import vo.ReviewVo;

public class Main {

	static Scanner sc = new Scanner(System.in);

	static String nickname;
	static String ID;

	static List<LoginVo> list = null;

	private static void main_login_disp() {

		while (true) {

			System.out.println("☆★☆★☆★☆★☆★뭅썰☆★☆★☆★☆★☆★");
			System.out.println("로그인 [1] ");
			System.out.println("회원 가입[2]");
			System.out.println("끝내기[3]");

			try {
				int res = sc.nextInt();

				sc.nextLine(); // 입력구분자제거
				if (res >= 4) {
					System.out.println("잘못 입력되었습니다.");

					continue;

				}
				if (res == 1) {

					login();

				} else if (res == 2) {

					sign_up();

				} else if (res == 3) {
					// 프로그램 종료
					return;
				}
			} catch (Exception e) {
				System.out.println("잘못 입력되었습니다.");
				sc = new Scanner(System.in);
			}
		}

	}

	// 회원가입
	private static void sign_up() {

		System.out.println("@@@@@@회원가입@@@@@@");

		// insert 하기 위한 vo 생성
		// LoginVo vo = new LoginVo();

		// 이미 있는 아이디인지 조회하기
		list = LoginDao.getInstance().selectList();

		OUT1: while (true) {

			System.out.println("회원가입을 그만 두시려면 Y를, 계속 진행하시려면 아무키를 누르세요.");
			String yn = sc.nextLine();

			if (yn.equalsIgnoreCase("Y")) {
				return;
			}

			System.out.println("\n회원 가입할 ID를 입력하세요.");
			System.out.print("ID >> ");
			String new_id = sc.nextLine();

			if (isKorean(new_id)) {
				System.out.println("\nID입력시 한글, 공백은 입력이 불가능합니다.");
				System.out.println();
				continue OUT1;
			}

			LoginVo vo = LoginDao.getInstance().selectOneFromID(new_id);

			if (vo != null) {
				System.out.println("\n이미 존재하는 아이디 입니다.");
				continue OUT1;
			}

			vo = new LoginVo();

			OUT2: while (true) {

				System.out.println("\n비밀번호를 입력하세요.");
				System.out.print("비밀번호 >> ");
				String new_pwd = sc.nextLine();

				if (isKorean(new_pwd)) {
					System.out.println("\n비밀번호는 한글, 공백은 입력이 불가능합니다.");

					continue OUT2;
				}
				// 비밀번호 체크 완료했으면 포장
				vo.setPassword(new_pwd);
				break;
			}

			OUT3: while (true) {
				System.out.println("\n닉네임을 입력하세요.");
				System.out.print("닉네임 >> ");
				String new_nick = sc.nextLine();

				LoginVo vo2 = LoginDao.getInstance().selectOneFromNickname(new_nick);

				// vo2값이 널이 아니면 즉 테이블에 데이터가 있으면
				if (vo2 != null) {
					System.out.println("\n이미 존재하는 닉네임 입니다.");
					continue OUT3;
				}

				// 닉네임 포장
				vo.setNickname(new_nick);
				break;

			}

			vo.setUserid(new_id);

			// 데이터를 진짜 준것
			int res = LoginDao.getInstance().signUp_insert(vo);

			System.out.println("회원가입 완료!!");

			return;

		} // while - end;

	}

	private static void login() {
		
		// 조회하기 위해 Dao객체 생성
		//list = LoginDao.getInstance().selectList();

		for (int i = 1; i <= 5; i++) {

			System.out.println("               [로그인]");
			System.out.println();
			
			
			System.out.print("아이디 : ");
			String id_check = sc.nextLine();

			LoginVo vo = LoginDao.getInstance().selectOneFromID(id_check);

			if (vo == null) {
				System.out.println("\n존재하지 않는 아이디입니다.");
				
				
				System.out.printf("\n %d회 로그인에 실패하였습니다.\n",i);
				continue ;
			}
			
			
			int count = 0;
			while (count<5) {
				
				System.out.print("비밀번호 : ");
				String pwd_check = sc.nextLine();

				if (!pwd_check.equals(vo.getPassword())) {
					System.out.println("비밀번호가 틀리셨습니다. ");
					
					count++;
					System.out.printf("비밀번호 입력 실패 %d회!! \n",count);
					
					
					
					continue;
				}
				
				//회원 가입 됐으면
				nickname = vo.getNickname();
				ID = vo.getUserid();
				
				System.out.printf("%s 님 환영합니다!! \n", nickname);
				
				main_main1_1_disp();

				
				// 메인종료되면 리턴해서 호출한 곳으로
				return;
			}

			
			
			
//			  아이디 비번 닉네임 
//			  'id1'      '123123'   '박평식의5점' 
//			  'my123',   '1q2w3e4r' '우주명작7광구'
//			  'bonglove' 'bong123'  '봉준호사랑해' 
//			  'kimchi12' 'kimchi12' 'kimchi12' 
//			  'gogo0325' '123123'   '영화학살자'

			
			
			
			
			
			
			
//			for (LoginVo a : list) {
//				// 아이디 체크
//				if (id_check.equals(a.getUserid())) {
//					// 비번체크
//					if (pwd_check.equals(a.getPassword())) {
//						System.out.println("\n로그인 완료...!!");
//						System.out.println();
//
//						nickname = a.getNickname();
//						ID = a.getUserid();
//
//						System.out.printf("%s 님 환영합니다!! \n", nickname);
//
//						main_main1_1_disp();
//
//						// 메인종료되면 리턴해서 호출한 곳으로
//						return;
//
//					}
//
//				} // id if - end;
//
//			} // 회원 check 종료
//
//			System.out.printf("로그인 실패 %d회!! \n", i + 1);
//			System.out.println();
//
//			if ((i + 1 == 5)) {
//				System.out.println();
//				System.out.println();
//				System.out.println("로그인에 실패하였습니다.");
//				System.out.println();
//				System.out.println();
//				System.out.println();
//			}

		} // 로그인 for end;

	}

	// 카테고리 디스플레이
	private static void main_main1_1_disp() {
		// TODO Auto-generated method stub

		while (true) {

			System.out.println("메인화면");
			System.out.println("뭅썰에 오신것을 환영합니다.");
			System.out.println("영화장르를 선택하세요.");

			List<CategoryVo> list = MovieDao.getInstance().select_Category_List();

			for (CategoryVo vo : list) {

				System.out.println("[" + vo.getCateno() + "] " + vo.getCatename());

			}

			System.out.println("[7] 내가 쓴 글 보러가기.");
			System.out.println("[8] 전체 리뷰 조회하기");
			System.out.println("[9] 로그아웃");

			try {
				int choice = sc.nextInt();
				sc.nextLine();

				if (choice >= 10) {
					System.out.println("잘못 입력되었습니다.");
					continue;

				}

				switch (choice) {
				case 1:
					main_main_disp(1);
					break;
				case 2:
					main_main_disp(2);
					break;
				case 3:
					main_main_disp(3);
					break;
				case 4:
					main_main_disp(4);
					break;
				case 5:
					main_main_disp(5);
					break;
				case 6:
					main_main_disp(6);
					break;
				case 7:
					review_disp_user();
					break;
				case 8:
					//전체 리뷰 테이블 조회
					review_all();
					break;
				case 9:
					return;
				}

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("잘못된 입력입니다.");
				sc = new Scanner(System.in);

			}

		} // while - end

	}

	private static void review_all() {
		// TODO Auto-generated method stub
		
		String[] menu = { "글번호", "영화제목", "내용", "작성자", "작성일자" };

		
		System.out.println(
				"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");
		System.out.printf("| %6s | %-16s\t  | %-32s\t\t | %-8s\t | %-12s|\n", menu[0], menu[1],
				menu[2], menu[3], menu[4]);
		System.out.println(
				"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");

		List<ReviewVo> all_list = new ArrayList<ReviewVo>();
		all_list = ReviewDao.getInstance().selectList_All();

		for (ReviewVo vo : all_list) {
			System.out.printf("| %7d | %-16s\t  | %-32s\t\t | %-8s\t | %-12s |\n", vo.getGeulno(),
					vo.getTitle(), vo.getGeultext(), vo.getNickname(), vo.getGeulDate().substring(0, 10));
			System.out.println(
					"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");

		}
	}

	// 카테고리의 영화 디스플레이
	private static void main_main_disp(int cateno) {
		// TODO Auto-generated method stub
		while (true) {
			// System.out.println("메인화면입니다.");
			// System.out.println("뭅썰에 오신걸 환영합니다!!!!");
			System.out.println("영화를 선택하세요");
			System.out.println("이전 화면으로 돌아가시려면 'n'을 입력하세요.");

			// 영화테이블을 조회해서 내용을 1~5까지의 내용을 읽어와서 동적으로 출력해주는 거
			// MovieVo vo = new MovieVo();
			List<MovieVo> movie_list = new ArrayList<MovieVo>();
			List<String> movie_list2 = new ArrayList<>();
			movie_list = MovieDao.getInstance().selectList(cateno);
			

			
			// 테이블에서 동적으로 영화제목 읽어오기
			for (MovieVo vo : movie_list) {

				System.out.println("["+ vo.getTitle() + "]");
				movie_list2.add(vo.getTitle());
		
			}
			
			//★★★★★★★★잘못 입력한 경우와 나가는 경우를 구분해서 구현★★★★★★
			
			
			String res = sc.nextLine();

				if (movie_list2.contains(res)) {
					review_disp(res);
				} else if(res.equalsIgnoreCase("n")){
					return;
				} else {
		
					System.out.println("장르에 포함되지 않은 영화입니다, 다시 입력해주세요.");
					continue;
				}
			
				
			

		}

	}

//	private static void main_main_disp() {
//		// TODO Auto-generated method stub
//
//		while (true) {
//			System.out.println("메인화면입니다.");
//			System.out.println("뭅썰에 오신걸 환영합니다!!!!");
//			System.out.println("메뉴를 선택하세요");
//
//			// 영화테이블을 조회해서 내용을 1~5까지의 내용을 읽어와서 동적으로 출력해주는 거
//			// MovieVo vo = new MovieVo();
//			List<MovieVo> movie_list = new ArrayList<MovieVo>();
//			movie_list = MovieDao.getInstance().selectList();
//
//			// 테이블에서 동적으로 영화제목 읽어오기
//			for (MovieVo vo : movie_list) {
//				System.out.println("[" + vo.getMovieidx() + "] " + vo.getTitle());
//			}
//
//			/*
//			 * System.out.println("[1] 명작영화"); System.out.println("[2] 졸작영화");
//			 * System.out.println("[3] 킬링타임영화"); System.out.println("[4] 아무거나");
//			 * System.out.println("[5] 모시깽이");
//			 */
//
//			System.out.printf("[6] %s 님이 쓴글 확인하기\n", nickname);
//			System.out.println("[7] 로그아웃");
//
//			try {
//				int res = sc.nextInt();
//
//				sc.nextLine(); // 입력구분자제거
//				if (res >= 8) {
//					System.out.println("잘못 입력되었습니다.");
//					continue;
//				}
//
//				switch (res) {
//				case 1:
//					review_disp(1);
//					break;
//
//				case 2:
//					review_disp(2);
//					break;
//
//				case 3:
//					review_disp(3);
//					break;
//
//				case 4:
//					review_disp(4);
//					break;
//
//				case 5:
//					review_disp(5);
//					break;
//
//				// case 6: review_disp(6); break;
//				case 6:
//					review_disp_user();
//					break;
//
//				case 7:
//					System.out.println("\n 로그아웃 합니다.\n 이용해 주셔서 감사합니다.");
//					return;
//				}
//
//			} catch (Exception e) {
//				System.out.println("잘못 입력되었습니다.");
//				sc = new Scanner(System.in);
//			}
//
//		}
//
//	}

	// 로그인한 사용자가 작성한 리뷰 조회하기
	private static void review_disp_user() {
		// TODO Auto-generated method stub

		while (true) {

			System.out.println(nickname + "님이 작성한 리뷰 목록입니다.");

			String[] menu = { "글번호", "영화제목", "내용", "작성자", "작성일자" };
			
			
			System.out.println(
					"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");
			System.out.printf("| %6s | %-16s\t  | %-32s\t\t | %-8s\t | %-12s|\n", menu[0], menu[1],
					menu[2], menu[3], menu[4]);
			System.out.println(
					"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");

			List<ReviewVo> select_review_list = new ArrayList<ReviewVo>();
			select_review_list = ReviewDao.getInstance().selectList_UserOnly(nickname);


			for (ReviewVo vo : select_review_list) {
				System.out.printf("| %7d | %-16s\t  | %-32s\t\t | %-8s\t | %-12s |\n", vo.getGeulno(),
						vo.getTitle(), vo.getGeultext(), vo.getNickname(), vo.getGeulDate().substring(0, 10));
				System.out.println(
						"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");

			}

			System.out.println("1.수정\n2.삭제\n3.이전 화면으로..");

			// 다른 숫자를 입력했을때, 어떻게 할지 에러처리 해야함
			try {
				int choice = sc.nextInt();
				sc.nextLine();

				if (choice >= 4) {
					System.out.println("잘못 입력되었습니다.");
					continue;
				}

				switch (choice) {
				case 1:
					update_review();
					break;
				case 2:
					delete_review();
					break;
				case 3:
					return;
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("잘못 입력하였습니다.");
				sc = new Scanner(System.in);
				continue;
			} // catch

		} // while - end

	}

	// 메인에서 내 리뷰만 보일때 메소드
	private static void delete_review() {

		while (true) {

			System.out.println("삭제할 리뷰의 번호를 입력하세요");

			try {

				int geulno = sc.nextInt();
				int res = ReviewDao.getInstance().delete_review(geulno);

				System.out.println("리뷰가 삭제되었습니다.");

				return;

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("글번호만 입력해 주세요.");
				sc = new Scanner(System.in);
				continue;
			}

		} // while - end

	}

	// 내 리뷰만 보일때 메소드
	private static void update_review() {

		while (true) {

			System.out.println("수정할 리뷰의 글번호를 입력하세요");

			try {

				int geulno = sc.nextInt();
				sc.nextLine();

				System.out.println("수정할 리뷰를 한줄이내 입력하세요");
				String geultext = sc.nextLine();
				// 현재 날짜 구하기
				LocalDate now = LocalDate.now();
				// 포맷적용해서 원하는 날짜만 가져오기
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

				String geulDate = now.format(formatter);

				ReviewVo vo = new ReviewVo();

				vo.setGeultext(geultext);
				vo.setGeulDate(geulDate);
				vo.setGeulno(geulno);

				ReviewDao.getInstance().update_review(vo);

				System.out.println("리뷰 수정이 완료됐습니다.");
				System.out.println();

				return;

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("글번호만 입력해 주세요.");
				sc = new Scanner(System.in);
				continue;

			}

		} // while - end

	}

	// 영화목록에서의 내 리뷰와 타유저 리뷰와 구별하기위한 메소드
	private static void update_onlymine() {

		OUT: while (true) {

			System.out.println("수정할 리뷰의 글번호를 입력하세요");

			try {

				int geulno = sc.nextInt();
				sc.nextLine();

				List<ReviewVo> list = ReviewDao.getInstance().selectList_UserOnly(geulno);

				for (ReviewVo a : list) {

					// 아이디 체크
					if (nickname.equals(a.getNickname())) {

						System.out.println("수정할 리뷰를 한줄이내 입력하세요");
						String geultext = sc.nextLine();
						// 현재 날짜 구하기
						LocalDate now = LocalDate.now();
						// 포맷적용해서 원하는 날짜만 가져오기
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

						String geulDate = now.format(formatter);

						ReviewVo vo = new ReviewVo();

						vo.setGeulno(geulno);
						vo.setGeultext(geultext);
						vo.setGeulDate(geulDate);

						int res = ReviewDao.getInstance().update_review(vo);

						System.out.println("리뷰 수정이 완료됐습니다." + res);
						System.out.println();
						break;

					} else {
						System.out.println("본인이 작성한 글이 아닙니다.");
						continue OUT;
					}

				}

				// 글 수정 완료시
				return;

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("글번호만 입력해 주세요.");
				sc = new Scanner(System.in);
				continue;
			}

		} // while - end

	}

	// 영화목록에서의 내 리뷰와 타유저 리뷰와 구별하기위한 메소드
	private static void delete_onlymine() {

		OUT: while (true) {

			System.out.println("삭제할 리뷰의 글번호를 입력하세요");

			try {

				int geulno = sc.nextInt();
				sc.nextLine();

				// geulno에 부합하는 nickname이 들어있는 list
				List<ReviewVo> list = ReviewDao.getInstance().selectList_UserOnly(geulno);
				for (ReviewVo a : list) {
					if (nickname.equals(a.getNickname())) {

						int res = ReviewDao.getInstance().delete_review(geulno);
						System.out.println("리뷰가 삭제됐습니다.");
						break;

					} else {
						System.out.println("본인이 작성한 글이 아닙니다.");
						continue OUT;
					}

				} // for 문 끝

				// 글 삭제 완료시
				break;

			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요.");
				sc = new Scanner(System.in);
				continue;
				// TODO: handle exception
			}

		} // while end

	}

	// 무비 리스트 조회하기
	private static void review_disp(String title) {
		// TODO Auto-generated method stub

		while (true) {

			String[] menu = { "글번호", "영화제목", "내용", "작성자", "작성일자" };
			
			
			System.out.println(
					"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");
			System.out.printf("| %6s | %-16s\t  | %-32s\t\t | %-8s\t | %-12s|\n", menu[0], menu[1],
					menu[2], menu[3], menu[4]);
			System.out.println(
					"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");

			List<ReviewVo> select_review_list = new ArrayList<ReviewVo>();
			select_review_list = ReviewDao.getInstance().selectList(title);


			for (ReviewVo vo : select_review_list) {
				System.out.printf("| %7d | %-16s\t  | %-32s\t\t | %-8s\t | %-12s |\n", vo.getGeulno(),
						vo.getTitle(), vo.getGeultext(), vo.getNickname(), vo.getGeulDate().substring(0, 10));
				System.out.println(
						"+ - - - - + - - - - - - - - - - - + - - - - - - - - - - - - - - - - - - - - - - -+ - - - - - - - + - - - - - - -+");

			}

			System.out.println("1.글쓰기\n2.수정\n3.삭제\n4.내가 쓴글 확인\n5.이전 화면으로..");

			try {

				int choice = sc.nextInt();
				sc.nextLine();

				if (choice >= 6) {
					System.out.println("잘못 입력하였습니다.");
					continue;
				}

				switch (choice) {
				case 1:
					//write_my_review();
					write_review(title);
					break;
				case 2:
					update_onlymine();
					break;
				case 3:
					delete_onlymine();
					break;
				case 4:
					review_disp_user();
					break;
				case 5:
					return;

				}

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("잘못 입력하였습니다.");
				sc = new Scanner(System.in);
				continue;
			}

		} // 출력화면 while - end

	}

	
	// 장르를 선택하고 영화를 고른 후에 리뷰 작성해서 삽입하는 부분
	private static void write_review(String movietitle) {
		// TODO Auto-generated method stub
		String title;
		String geulText;
		String userId = ID;
		String geulDate;
		int movieNo;
		
		// 현재 날짜 구하기
		LocalDate now = LocalDate.now();
		// 포맷적용해서 원하는 날짜만 가져오기
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		geulDate = now.format(formatter);
		
		title = movietitle;
		movieNo = MovieDao.getInstance().selectMovieNo(title);
		
		System.out.print("\n영화에 대한 리뷰를 작성해주세요.단, 글자수는 20자를 넘을 수 없습니다.");
		geulText = sc.nextLine();
		
		ReviewVo vo = new ReviewVo(movieNo, geulText, userId, geulDate);
		
		int res = ReviewDao.getInstance().insert_review(vo);
		
		if (res == 0) {
			System.out.println("글 쓰기 실패!");
		} else {
			System.out.println("글 쓰기 성공!");
		}
		
		
	}

	// 아이디 검사 메소드 한국어와 띄어쓰기가 들어왔으면
	public static boolean isKorean(String str) {
		return Pattern.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣!?()\s]+.*", str.replace("\n", ""));

	}

	// 메인메소드 시작

	public static void main(String[] args) {

		main_login_disp(); // 이 함수에서 return하면 끝남

		System.out.println("프로그램 종료");

	}
}
