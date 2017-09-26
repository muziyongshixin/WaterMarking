
package  serverMain;

import net.sf.json.JSONObject;

/**
 * Created by 32706 on 2017/3/2.
 */


public class Function {


    public static JSONObject Parameter_extraction(JSONObject in)//解析传进来的参数
    {
        JSONObject rt=new JSONObject();
        if( in.getString("function")==null||in.getString("function").equals(""))
        {
            rt.put("state","failed");
            rt.put("wrongInfo","no function parameter");
        }
        else
        {
           execute(in,rt);
        }
        return rt;
    }


    private static void execute(JSONObject info,JSONObject rt) {

        String func=info.getString("function");
        switch (func) {
            case "LOGIN": {
                User u = identification(info.getString("phone"),info.getString("password"));//验证用户名和密码
                if (u != null) {
                    rt.put("state","successful");
                    String token = TokenProcessor.generateToken(u.getPhoneNum(), true);//根据手机号生成token
                    u.set_token(token);
                    User_in_System.Insert_user_to_system(token, u);//将用户存在系统内存中
                    rt.put("token",token);

                } else {
                    System.out.println("登录失败==========================Function.execute.case 登录");
                    rt.put("state","failed");
                    rt.put("wrongInfo","登录失败==========================Function.execute.case 登录");
                    return;
                }

                break;
            }
            //新增加的资产
            case "UPLOAD": {
                User u = User_in_System.get_user_in_system(info.getString("token"));
                if (u != null) {
                    if(info.getString("pic")!=null)
                    {
                        u.upload_pic(info,rt);
                    }
                    else
                    {
                        System.out.println("上传失败，upload_pic返回错误===================Function.execute.case 上传");
                        rt.put("state","failed");
                        rt.put("wrongInfo","上传失败，upload_pic返回错误===================Function.execute.case 上传");
                        return;
                    }

                }else {
                    System.out.println("上传失败,用户未登录==========================Function.execute.case 上传");
                    rt.put("state","failed");
                    rt.put("wrongInfo","上传失败,用户未登录==========================Function.execute.case 上传");
                    return;
                }

                break;
            }
            case "DOWNLOAD": {
                User u = User_in_System.get_user_in_system(info.getString("token"));
                if (u != null) {

                   if(! u.download_pic(info,rt))//如果返回为false
                   {
                       System.out.println("下载失败，user类返回false=================Function.execute.case:下载");
                   }


                } else {
                    System.out.println("下载失败,用户未登录==========================Function.execute.case:下载");
                    rt.put("state","failed");
                    rt.put("wrongInfo","下载失败,用户未登录==========================Function.execute.case:下载");
                    return;
                }


                break;
            }
            case "ASSETNO":
            {
                User u = User_in_System.get_user_in_system(info.getString("token"));
                if (u != null) {
                    String asset_no = String.valueOf(u.getAssetMaxId());
                    rt.put("asset_no",asset_no);
                    rt.put("state","successful");
                } else {
                    System.out.println("获取资产序号失败,用户未登录==========================Function.execute.case:下载");
                    rt.put("state","failed");
                    rt.put("wrongInfo","获取资产序号失败,用户未登录==========================Function.execute.case:下载");
                    return;
                }


                break;
            }

            case "USERINFO":
            {
                

                break;
            }
            case "DELETE":
            {
                User u = User_in_System.get_user_in_system(info.getString("token"));
                if (u != null) {
                    if( u.delete_asset(info.getString("asset_no"),rt)){
                        System.out.println("删除成功");
                    }else{
                        System.out.println("删除出现问题" + rt.getString("wrongInfo"));
                    }
                }else{
                    System.out.println("删除失败,用户未登录==========================Function.execute.case:下删除");
                    rt.put("state","failed");
                    rt.put("wrongInfo","删除失败,用户未登录==========================Function.execute.case:删除");
                }
                break;
            }
            case "LOGOUT":
            {
                break;
            }
            default: {
                rt.put("state","failed");
                rt.put("wrongInfo","功能解析失败==========================Function.execute.default");
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
