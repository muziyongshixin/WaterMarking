

/**
 * Created by 32706 on 2017/3/2.
 */


public class Function {


    public static ConnectionData Parameter_extraction(ConnectionData in)//解析传进来的参数
    {
        ConnectionData rt=new ConnectionData();
        if( in.getFunction()==null||in.getFunction().equals(""))
        {
            rt.setState("failed");
            rt.setWrong_info("no function parameter");
        }
        else
        {
           execute(in,rt);
        }
        return rt;
    }


    private static void execute(ConnectionData info,ConnectionData rt) {

        String func=info.getFunction();
        switch (func) {
            case "LOGIN": {
                User u = identification(info.getPhone(),info.getPassword());//验证用户名和密码
                if (u != null) {
                    String token = TokenProcessor.generateToken(u.getPhoneNum(), true);//根据手机号生成token
                    u.set_token(token);
                    User_in_System.Insert_user_to_system(token, u);//将用户存在系统内存中
                    rt.setToken(token);

                } else {
                    System.out.println("登录失败==========================Function.execute.case 登录");
                    rt.setState("failed");
                    rt.setWrong_info("登录失败==========================Function.execute.case 登录");
                    return;
                }

                break;
            }
            case "UPLOAD": {
                User u = User_in_System.get_user_in_system(info.getToken());
                if (u != null) {
                    if(info.getPicture_list()!=null)
                    {
                        u.upload_pic(info,rt);
                    }
                    else
                    {
                        System.out.println("上传失败，upload_pic返回错误===================Function.execute.case 上传");
                        rt.setState("failed");
                        rt.setWrong_info("上传失败，upload_pic返回错误===================Function.execute.case 上传");
                        return;
                    }

                }else {
                    System.out.println("上传失败,用户未登录==========================Function.execute.case 上传");
                    rt.setState("failed");
                    rt.setWrong_info("上传失败,用户未登录==========================Function.execute.case 上传");
                    return;
                }

                break;
            }
            case "DOWNLOAD": {
                User u = User_in_System.get_user_in_system(info.getToken());
                if (u != null) {

                   if(! u.download_pic(info,rt))//如果返回为false
                   {
                       System.out.println("下载失败，user类返回false=================Function.execute.case:下载");
                   }


                } else {
                    System.out.println("下载失败,用户未登录==========================Function.execute.case:下载");
                    rt.setState("failed");
                    rt.setWrong_info("下载失败,用户未登录==========================Function.execute.case:下载");
                    return;
                }


                break;
            }
            case "USERINFO":
            {


                break;
            }
            case "REFRESH" :
            {

                break;
            }
            case "MODIFY":
            {
                break;
            }
            case "DELETE":
            {

            }
            case "LOGOUT":
            {
                break;
            }
            default: {
                rt.setState("failed");
                rt.setWrong_info("功能解析失败==========================Function.execute.default");
                return;
            }


        }


    }


    private static User identification(String phone, String password) {
        User rt = Database.User_identification(phone, password);
        if (rt == null) {
            System.out.println("登录失败===========Function.identification");
        } else {
            String token = "";
            rt.set_token(token);
        }


        return rt;
    }




    public static void main(String[] args) {
        // TODO Auto-generated method stub



    }




}
