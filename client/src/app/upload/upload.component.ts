import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { RemoteService } from '../remote.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-upload,',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent {

  fileName = '';
  formData = new FormData();
  responseData = '';
  fileLayout = '';
  options: string[] = ['Person', 'Person Long Names', 'Car'];
  optionMapper: { [key: string]: string; } = {
    'Person' : 'PERSON',
    'Car' : 'CAR' ,
    'Person Long Names' : 'PERSON2'
  };


  constructor(private http: HttpClient, private remoteService: RemoteService) {}

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.fileName = file.name;
      this.formData.append("flatFile", file);
    } 
  }

  selectUploadFile(): void {
   
    this.remoteService.uploadFile(this.formData,  this.optionMapper[this.fileLayout])
    .subscribe({
      next: (data) => {
        console.log(data.body);
        this.responseData = JSON.stringify(data.body, null, 2);
      },
      error: (error: HttpErrorResponse) => {
        const errorMessage = error.error.message;
        console.log(error.error);
      }
    })
  }



}
